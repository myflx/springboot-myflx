# 深入Redis

## Redis基础

[参考]()

### Redis可以做什么？

- Redis 可以理解为数据库，nosql数据库的一种类型，肯定可以用来作为存储的。数据可冷可热，存储时间可长可短。类似的有MemCache，但相对来书redis更容易理解和使用。
- 缓存是Redis应用最广泛的地方。从Redis数据结构上讲在业务上具有很多的用途。例如
  - 计数器
  - 分布式锁
  - 队列
  - 使用不同数据结构对热数据进行缓存string缓存普通对象如token，hset缓存对象，lpush分页，sadd去重，zadd排序。
  - 使用set getbit 操作位图实现大数据的存储。并对数据查找统计。
  - 也用作系统限流的。
  - 也有一些高级用法，布隆过滤器防击穿或者定向推荐，puv的统计用HyperLogLog。GeoHash 统计附近的人。
  - Redis最核心的就是他的单线程和数据算法。
- 更Memcache相比较
  - 数据结构丰富
  - 算法性能好所以速度快
  - ....

### Redis安装及配置

​	[查看](Redis安装及配置.md)

### Redis基础数据结构

​		Redis 有2中数据结构分别为：string（字符串） ，list（列表），hash（字典），set（集合）和zset（有序集合）。

#### string（字符串）

- 字符串是redis最简单的数据结构。Redis所有的key都一样区别主要是value，string类型的内部结构就是一个字符数组。
- 字符串分配空间时会分配冗余空间避免频繁分配。所以一般分配的空间会大于实际长度。1M以内空间扩容为当前的一倍，超过1M最大扩1M的空间。字符串最大长度为512MB。

批量命令 一次性操作减少网络开销

> mset : 批量设置值
>
> mget：批量获取值

#### list（列表）

​		list 相当于Java 语言里边的LinkedList 所以插入和移除速度比较快时间复杂度为O(1)，通常使用异步队列。当列表最后一个数据弹出，数据结构被自动删除，内存被回收。

同向进出为栈：rpush+rpop，lpush+lpop

反向进出为队列：rpush+lpop，lpush+rpop

一些慢操作，需要谨慎使用

```java
> lindex book 1 #获取列表book 第2个位置元素，为O(n) 慎用
> lrange book 0 -1 #获取所有元素 O(n) 慎用
#截取固定范围的列表 通常用来实现一个定长的链表,获取前10名数据
> ltrim book 1 -1 #O(n) 慎用
#清空了列表，区间范围长度为负数
> ltrim book 1 0 
```

​		Redis list数据结构底层存储的不是简单的Linked List，是一种叫quicklist的数据结构。首先在元素较少的时候使用连续内存存储，这种结构叫做压缩列表 ziplist。当元素较多的时候才会改成quicklist。这个quicklist将多个ziplist通过双向指针串起来使用。不会出现太大的冗余空间。



#### hash（字典）

​		同Java HashMap 一样都是数组+链表结构。但Redis 字典的值只能是字符串，并且他们的rehash的方式不一样。Redis为了追求高性能，不能堵塞服务，所以采用了渐进式的rehash策略。

​		渐进式的rehash策略，redis rehash的时候会同时保留新旧两个hash位，查询的时候也会查询两个hash结构，然后会在后续的定时任务中慢慢将就结构中的数据迁移到新结构。搬迁完成就是使用新的hash结构取而代之，旧结构会被删除，内存会被回收。

​		hash也有缺点，hash结构的存储会高于单个字符串。使用何种结构要看实际情况。

```java
127.0.0.1:6379> hset books java "think in java"
(integer) 1
127.0.0.1:6379> hset books go "now is go"
(integer) 1
127.0.0.1:6379> hset books python "make world easy"
(integer) 1
127.0.0.1:6379> hgetall books
1) "java"
2) "think in java"
3) "go"
4) "now is go"
5) "python"
6) "make world easy"
127.0.0.1:6379> hlen books
(integer) 3
127.0.0.1:6379> hget books java
"think in java"
127.0.0.1:6379> hset books go "now is go 2" #更新操作返回0
(integer) 0
127.0.0.1:6379> hget books go
"now is go 2"
127.0.0.1:6379> hmset java "hello world in java " go "lets go now"
(error) ERR wrong number of arguments for HMSET
127.0.0.1:6379> hmset books java "hello world in java " go "lets go now"
OK
127.0.0.1:6379> hgetall books
1) "java"
2) "hello world in java "
3) "go"
4) "lets go now"
5) "python"
6) "make world easy"
127.0.0.1:6379>
```

字典内的字段也可以进行计数操作

```java
127.0.0.1:6379> hset books number 100
(integer) 1
127.0.0.1:6379> hincrby books number 1
(integer) 101
127.0.0.1:6379>
```



#### set（集合）

​		Redis集合类似于Java里边的HashSet，他内部的键值对是无序的，唯一的。也相当于一个特殊的字典，value值都是一个null。只有当最后一个元素被移除，数据结构才会被删除，内存被回收。

​		可以用来存储不重复的结构，例如一次抽奖中一个用户只能中奖一次，可以存入用的主键。保证重中奖。

```java
127.0.0.1:6379> sadd books java
(integer) 1
127.0.0.1:6379> sadd books java python #重复的不会插入
(integer) 1
127.0.0.1:6379> sadd books java golang
(integer) 1
127.0.0.1:6379> sadd books c vb
(integer) 2
127.0.0.1:6379> smembers books #获取books对应的所有数据
1) "python"
2) "c"
3) "golang"
4) "java"
5) "vb"
127.0.0.1:6379> sismember books abc # 判断abc是否存在于books集合中
(integer) 0
127.0.0.1:6379> scard books #获取长度
(integer) 5
127.0.0.1:6379> spop books #弹出一个数据
"c"
127.0.0.1:6379>
```

#### zset（有序列表）

​		zset是redis最有特色的数据结构，他的结构类似于Java的SortedSet 和HashMap的结合体。一方面他是一个set保证value的唯一性，另一方面他给每个value赋予一个score代表value的排序权重，他的内部实现是一种叫做的"跳跃列表"的数据结构。通常用在有排序需求的业务上。

```java
127.0.0.1:6379> zadd books 9.0 "think in java"
(integer) 1
127.0.0.1:6379> zadd books 8.9 "java concurrency"
(integer) 1
127.0.0.1:6379> zadd books 8.5 "java cookie"
(integer) 1
127.0.0.1:6379> zrange books 0 -1 #获取所有（按照score asc排序）
1) "java cookie"
2) "java concurrency"
3) "think in java"
127.0.0.1:6379> zrevrange books 0 -1 #获取所有（按照score desc排序）
1) "think in java"
2) "java concurrency"
3) "java cookie"
127.0.0.1:6379> zcard books #相当于count()
(integer) 3
127.0.0.1:6379> zscore books "java concurrency" #获取数据的权重
"8.9000000000000004" #使用doule村存储会存在小数点失真的问题
127.0.0.1:6379> zrank books "java concurrency" #获取数据的排名，从0开始
(integer) 1
    			#根据范围（-∞，8.91]遍历zset 同时返回权重值。
127.0.0.1:6379> zrangebyscore books -inf 8.91 withscores
1) "java cookie"
2) "8.5"
3) "java concurrency"
4) "8.9000000000000004"
127.0.0.1:6379> zrem books "java concurrency" #移除value
(integer) 1
127.0.0.1:6379> zrange books  0 -1
1) "java cookie"
2) "think in java"
127.0.0.1:6379>
```

##### 跳跃列表







