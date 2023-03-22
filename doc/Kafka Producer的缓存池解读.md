## Kafka Producer的缓存池解读

`class:org.apache.kafka.clients.producer.internals.BufferPool`

`version:kafka-clients-3.1.1.jar`

***缓存池*** 是一种池化技术，属于对象池的一种，其缓存的对象是Java nio 中的ByteBuffer对象，目的是减少对象的频繁创建带来的消耗及其对象销毁之后的频GC。

缓冲池有不同开源的实现，本文主要解读Kafka Producer的缓存池实现。

注意：并不是完全没有GC，在一次场景下还是会有GC。另外在一些场景下会发生等待。用的不好也会造成性能问题。

### 核心成员

```java
public class BufferPool {
    private final long totalMemory;//总内存
    private final int poolableSize;//缓冲对象（ByteBuffer）的大小
    private final ReentrantLock lock;//内存操作互斥锁
    private final Deque<ByteBuffer> free;//可用内存池
    private final Deque<Condition> waiters;//等待锁
    /** Total available memory is the sum of nonPooledAvailableMemory and the number of byte buffers in free * poolableSize.  */
    private long nonPooledAvailableMemory;//未入池的内存量（未使用的内存数量、初始等于总内存量）
    //监控统计相关的
    private final Metrics metrics;//指标
    private final Sensor waitTime;//传感器
    private final Time time;
    private boolean closed;
}
```

totalMemory : 总内存量，默认32M （33554432 字节）

对应配置: [buffer.memory](https://kafka.apache.org/documentation.html#producerconfigs_buffer.memory)

poolableSize: 缓存对象的大小，默认 16384 （16K）

对应配置：[batch.size](https://kafka.apache.org/documentation.html#producerconfigs_batch.size)

可用内存量Total available memory = nonPooledAvailableMemory + byte buffers in free * poolableSize

可用内存量<= 总内存量

分配内存的最大等待时间（KafkaProducer#maxBlockTimeMs）：默认 60000 (1 minute) 

对应配置：[max.block.ms](https://kafka.apache.org/documentation.html#producerconfigs_max.block.ms)

### 核心操作-分配内存

分配内存的场景较多，当前part对整体的分配过程有个轮廓，详细的点会在闭环的Case分析中解读。

内存分配过程中的核心流程如下：

- 上锁
- 池中有ByteBuffer直接取用
- 内存量够，但是缓存池中没有ByteBuffer，维护内存量值并分配内存
- 内存量不够，加锁并自旋等待，获取池中的可用ByteBuffer对象或者获取到内存值之后自行创建。
- 通知等待对象（内存充足时），开锁
- 获取到ByteBuffer对象直接返回。
- 直接分配ByteBuffer对象。

配置的内存大小不能少于标准尺寸

```java
int size = Math.max(this.batchSize, AbstractRecords.estimateSizeInBytesUpperBound(maxUsableMagic, compression, key, value, headers));
```

内存分配:

```java
//分配大小为size的内存，最长阻塞等待maxTimeToBlockMs毫秒的时间
public ByteBuffer allocate(int size, long maxTimeToBlockMs) throws InterruptedException {
    if (size > this.totalMemory)
        throw new IllegalArgumentException("Attempt to allocate " + size
                                           + " bytes, but there is a hard limit of "
                                           + this.totalMemory
                                           + " on memory allocations.");
    ByteBuffer buffer = null;
    this.lock.lock();
    if (this.closed) {
        this.lock.unlock();
        throw new KafkaException("Producer closed while allocating memory");
    }
    try {
        // check if we have a free buffer of the right size pooled
        if (size == poolableSize && !this.free.isEmpty())
            return this.free.pollFirst();
        // now check if the request is immediately satisfiable with the
        // memory on hand or if we need to block
        int freeListSize = freeSize() * this.poolableSize;
        if (this.nonPooledAvailableMemory + freeListSize >= size) {
            // we have enough unallocated or pooled memory to immediately
            // satisfy the request, but need to allocate the buffer
            freeUp(size);
            this.nonPooledAvailableMemory -= size;
        } else {
            // we are out of memory and will have to block
            int accumulated = 0;
            Condition moreMemory = this.lock.newCondition();
            try {
                long remainingTimeToBlockNs = TimeUnit.MILLISECONDS.toNanos(maxTimeToBlockMs);
                this.waiters.addLast(moreMemory);
                // loop over and over until we have a buffer or have reserved
                // enough memory to allocate one
                while (accumulated < size) {
                    long startWaitNs = time.nanoseconds();
                    long timeNs;
                    boolean waitingTimeElapsed;
                    try {
                        waitingTimeElapsed = !moreMemory.await(remainingTimeToBlockNs, TimeUnit.NANOSECONDS);
                    } finally {
                        long endWaitNs = time.nanoseconds();
                        timeNs = Math.max(0L, endWaitNs - startWaitNs);
                        recordWaitTime(timeNs);
                    }
                    if (this.closed)
                        throw new KafkaException("Producer closed while allocating memory");

                    if (waitingTimeElapsed) {
                        this.metrics.sensor("buffer-exhausted-records").record();
                        throw new BufferExhaustedException("Failed to allocate " + size + " bytes within the configured max blocking time "
                                                           + maxTimeToBlockMs + " ms. Total memory: " + totalMemory() + " bytes. Available memory: " + availableMemory()
                                                           + " bytes. Poolable size: " + poolableSize() + " bytes");
                    }
                    remainingTimeToBlockNs -= timeNs;

                    // check if we can satisfy this request from the free list,
                    // otherwise allocate memory
                    if (accumulated == 0 && size == this.poolableSize && !this.free.isEmpty()) {
                        // just grab a buffer from the free list
                        buffer = this.free.pollFirst();
                        accumulated = size;
                    } else {
                        // we'll need to allocate memory, but we may only get
                        // part of what we need on this iteration
                        freeUp(size - accumulated);
                        int got = (int) Math.min(size - accumulated, this.nonPooledAvailableMemory);
                        this.nonPooledAvailableMemory -= got;
                        accumulated += got;
                    }
                }
                // Don't reclaim memory on throwable since nothing was thrown
                accumulated = 0;
            } finally {
                // When this loop was not able to successfully terminate don't loose available memory
                this.nonPooledAvailableMemory += accumulated;
                this.waiters.remove(moreMemory);
            }
        }
    } finally {
        // signal any additional waiters if there is more memory left
        // over for them
        try {
            if (!(this.nonPooledAvailableMemory == 0 && this.free.isEmpty()) && !this.waiters.isEmpty())
                this.waiters.peekFirst().signal();
        } finally {
            // Another finally... otherwise find bugs complains
            lock.unlock();
        }
    }
    if (buffer == null)
        return safeAllocateByteBuffer(size);
    else
        return buffer;
}
```



### 核心操作-回收内存

内存回收过程比较简单，只回收标准大小的ByteBuffer对象。流程如下：

- 加锁
- 标准的ByteBuffer对象会进行回收
- 非标准对象废弃（GC掉），增加未入池的可用内存量
- 通知等待中的锁
- 解锁

```java
public void deallocate(ByteBuffer buffer, int size) {
    lock.lock();
    try {
        if (size == this.poolableSize && size == buffer.capacity()) {
            buffer.clear();
            this.free.add(buffer);
        } else {
            this.nonPooledAvailableMemory += size;
        }
        Condition moreMem = this.waiters.peekFirst();
        if (moreMem != null)
            moreMem.signal();
    } finally {
        lock.unlock();
    }
}
```

### 关键Case分析

标准内存：16K

总内存：32M

可用缓存池：`Deque<ByteBuffer> free`

#### Case1.分配超过总量内存

```java
//如果请求分配的内存超过总内存量直接抛异常
if (size > this.totalMemory)
    throw new IllegalArgumentException("Attempt to allocate " + size
                                       + " bytes, but there is a hard limit of "
                                       + this.totalMemory
                                       + " on memory allocations.");
```

#### Case2.分配标准内存，可用缓存池为空，总内存充足

申请内存量

```java
int freeListSize = freeSize() * this.poolableSize;//freeListSize=0
if (this.nonPooledAvailableMemory + freeListSize >= size) {
    // we have enough unallocated or pooled memory to immediately
    // satisfy the request, but need to allocate the buffer
    freeUp(size);//不会在可用缓存池中释放
    this.nonPooledAvailableMemory -= size;//扣减未入缓存的可用内存量
}
```

分配内存

```java
if (buffer == null)
    return safeAllocateByteBuffer(size);
else
    return buffer;
```

内存回收：

申请的内存是标准内存，回收的时候会进入到可以用缓存池中。nonPooledAvailableMemory的值不会改变

```java
if (size == this.poolableSize && size == buffer.capacity()) {
    buffer.clear();
    this.free.add(buffer);
}
```

#### Case3.分配标准内存，可用缓存池为空，总内存不足

```java
// we are out of memory and will have to block
int accumulated = 0;
//获取lock condition作为waiter进入等待列表
Condition moreMemory = this.lock.newCondition();
try {
    //自旋合计等待的最长时间
    long remainingTimeToBlockNs = TimeUnit.MILLISECONDS.toNanos(maxTimeToBlockMs);
    this.waiters.addLast(moreMemory);
    // loop over and over until we have a buffer or have reserved
    // enough memory to allocate one
    while (accumulated < size) {//未获取到量，保持自旋
        long startWaitNs = time.nanoseconds();
        long timeNs;
        boolean waitingTimeElapsed;
        try {
            //方法返回之前事件已经过去了（moreMemory.await() true是没有过去）
            waitingTimeElapsed = !moreMemory.await(remainingTimeToBlockNs, TimeUnit.NANOSECONDS);
        } finally {
            long endWaitNs = time.nanoseconds();
            timeNs = Math.max(0L, endWaitNs - startWaitNs);
            recordWaitTime(timeNs);
        }

        if (this.closed)
            throw new KafkaException("Producer closed while allocating memory");
		//等待超时了
        if (waitingTimeElapsed) {
            throw new TimeoutException("Failed to allocate memory within the configured max blocking time " + maxTimeToBlockMs + " ms.");
        }
		//下次
        remainingTimeToBlockNs -= timeNs;

        //本轮等待被唤醒了，当前的可用缓存池已有回收对象可以满足的申请
        if (accumulated == 0 && size == this.poolableSize && !this.free.isEmpty()) {
            buffer = this.free.pollFirst();//直接从队列中获取缓存对象
            accumulated = size;//记录获取的内存量
        } else {
            //此时可用缓存池是空的,只获取了部分的内存量，继续进入自旋等待可用缓存池的中归还的对象。
            freeUp(size - accumulated);//freeUp的量其实还是为0
            //此时拿到的量got最大为nonPooledAvailableMemory
            //nonPooledAvailableMemory<size 注意不会是相等的，所以说拿到的是部分的内存量
            int got = (int) Math.min(size - accumulated, this.nonPooledAvailableMemory);
            this.nonPooledAvailableMemory -= got;
            accumulated += got;
        }
    }
    // Don't reclaim memory on throwable since nothing was thrown
    accumulated = 0;
} finally {
    // When this loop was not able to successfully terminate don't loose available memory
    this.nonPooledAvailableMemory += accumulated;
    this.waiters.remove(moreMemory);
}
```

内存释放：

此时当前的请求处于可用内存的少于请求的内存的状态，剩余的内存量已经被当前请求占住了。当时还缺少部分数量内存来创建ByteBuff，所以要释放可用缓存池中的对象（进入GC）来用于满足当前请求的内存量。

上面的过程中释放的内存累积在nonPooledAvailableMemory上，所以要 -= got

```java
/**
 * Attempt to ensure we have at least the requested number of bytes of memory for 
 * allocation by deallocating pooled * buffers (if needed) 
 */
private void freeUp(int size) {
    while (!this.free.isEmpty() && this.nonPooledAvailableMemory < size)       	 
        this.nonPooledAvailableMemory += this.free.pollLast().capacity();
}
```

内存回收：

经过前面的自旋和可用缓存池的中缓存对象的释放，本次请求就获取到了充足的内存量，解锁之前就进入内存分配`safeAllocateByteBuffer(size)`

虽然当前过程中获取的ByteBuff不是一个已经入缓存池的对象，但是他是一个标准尺寸的缓存对象所以回收时依然会进入可用缓存池。

> 总结:
>
> 在获取内存的最后临界点会释放一定的已经入池的ByteBuff进入GC，并创建新的ByteBuff最后回收时依然会进入可用缓存池中缓存起来。

#### Case4.分配标准内存，可用缓存池中已有回收的内存

直接从可用缓存池中获取ByteBuffer

```java
// check if we have a free buffer of the right size pooled
if (size == poolableSize && !this.free.isEmpty())
    return this.free.pollFirst();
```



#### Case5.分配超标内存，剩余内存充足

```java
if (this.nonPooledAvailableMemory + freeListSize >= size) {
    // we have enough unallocated or pooled memory to immediately
    // satisfy the request, but need to allocate the buffer
    freeUp(size);//此处不会释放内存，下面会直接扣减需要的内存
    this.nonPooledAvailableMemory -= size;
}
```

超标的内存获取到内存量之后都会创建新的ByteBuff对象

内存回收：

因为尺寸超标所以只会回收内存量，ByteBuff会进入GC，分配超标内存的内存回收逻辑一样。后面不再赘述。

```java
this.nonPooledAvailableMemory += size;
```

#### Case6.分配超标内存，剩余内存充足

```java
if (this.nonPooledAvailableMemory + freeListSize >= size) {
    // we have enough unallocated or pooled memory to immediately
    // satisfy the request, but need to allocate the buffer
    freeUp(size);//此处会释放足够的内存用于申请内存的扣减
    this.nonPooledAvailableMemory -= size;
}
```

#### Case7.分配超标内存，总内存不足

具体的流程逻辑同标准内存分配

### 总结说明

- kafka的缓存池是以标准尺寸的ByteBuff为基础的
- 超标的ByteBuff在回收时会被抛弃，进入GC，配置时应结合具体的业务场景测试，尽量避免超标对象，可能会造成频繁的GC。
- 剩余内存充足，可用缓存池没有对象时会创建新的ByteBuff对象。合标的ByteBuff会进入可用缓存池中待用。
- 总内存不足时，可用缓存池中没有可用对象时，不管分配的尺寸是否超标都会进入自旋获取可用对象或者在临界点释放部分可用缓存池的对象。

最后，使用缓存池的时配置的ByteBuff的长度要尽量覆盖觉大多数的场景，但也不适合配置过大，以具体场景测试结果作为决策标准。总的缓存内存大小也要超过较高并发场景所需要的内存值，这个值过少也会导致一定程度的GC。

