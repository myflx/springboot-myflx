package com.myflx.common.classloader;

import java.util.concurrent.RecursiveTask;

/**
 * 现在让我们来实践下 ForkJoinPool 的功能：计算 π 的值。计算 π 的值有一个通过多项式方法，即：π = 4 * (1 - 1/3 + 1/5 - 1/7 + 1/9 - ……)，而且多项式的项数越多，计算出的 π 的值越精确。
 */
public class PiEstimateTask extends RecursiveTask<Double> {

    private final long begin;
    private final long end;
    private final long threshold; // 分割任务的临界值

    public PiEstimateTask(long begin, long end, long threshold) {
        this.begin = begin;
        this.end = end;
        this.threshold = threshold;
    }

    @Override
    protected Double compute() {
        if (end - begin <= threshold) {
            int sign; // 符号，多项式中偶数位取 1，奇数位取 -1（位置从 0 开始）
            double result = 0.0;
            for (long i = begin; i < end; i++) {
                sign = (i & 1) == 0 ? 1 : -1;
                result += sign / (i * 2.0 + 1);
            }
            return result * 4;
        }

        // 分割任务
        long middle = (begin + end) / 2;
        PiEstimateTask leftTask = new PiEstimateTask(begin, middle, threshold);
        PiEstimateTask rightTask = new PiEstimateTask(middle, end, threshold);

        leftTask.fork();  // 异步执行 leftTask
        rightTask.fork(); // 异步执行 rightTask

        double leftResult = leftTask.join();   // 阻塞，直到 leftTask 执行完毕返回结果
        double rightResult = rightTask.join(); // 阻塞，直到 rightTask 执行完毕返回结果

        return leftResult + rightResult; // 合并结果
    }
}
