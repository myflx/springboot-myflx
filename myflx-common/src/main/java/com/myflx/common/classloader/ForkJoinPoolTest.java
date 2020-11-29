package com.myflx.common.classloader;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class ForkJoinPoolTest {
    public static void main(String[] args) throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        // 计算 10 亿项，分割任务的临界值为 1 千万
        PiEstimateTask task = new PiEstimateTask(0, 1_000_000_000, 10_000_000);

        double pi = forkJoinPool.invoke(task); // 阻塞，直到任务执行完毕返回结果

        Future<Double> future = forkJoinPool.submit(task); // 不阻塞

        System.out.println("1. π 的值：" + pi);
        System.out.println("2. π 的值：" + future.get());
        System.out.println("π 的值：" + pi);
        System.out.println("future 指向的对象是 task 吗：" + (future == task));

        forkJoinPool.shutdown(); // 向线程池发送关闭的指令
    }
}
