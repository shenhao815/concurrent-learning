package com.it.DIY;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestShutDown {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        final Future<Integer> result1 = pool.submit(() -> {
            log.debug("task 1 running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("task 1 finished");
            return 1;
        });
        final Future<Integer> result2 = pool.submit(() -> {
            log.debug("task 2 running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("task 2 finished");
            return 2;
        });
        final Future<Integer> result3 = pool.submit(() -> {
            log.debug("task 3 running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("task 3 finished");
            return 3;
        });

        log.debug("shutdown");
        /*
            线程池状态变为 SHUTDOWN
            不会接收新任务
            但已提交的任务会执行完
            此方法不会阻塞调用线程的执行
         */
        //pool.shutdown();
        //pool.awaitTermination(3, TimeUnit.SECONDS);

        /*
         * 线程池状态变为 STOP
         * 不会接收新任务
         * 会将队列中的任务返回，（执行中的任务不会返回）
         * 并用interrupt 的方式中断正在执行的任务
         */
        final List<Runnable> runnables = pool.shutdownNow();
        runnables.forEach(runnable -> {
            System.out.println(runnable);
        });
        System.out.println(runnables.size());
    }
}
