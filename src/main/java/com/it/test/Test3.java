package com.it.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ch
 * @date 2020-9-25
 * @description
 */
@Slf4j
public class Test3 {
    static int r1 = 0;
    static int r2 = 0;
    public static void main(String[] args) throws InterruptedException {
        test2();
    }
    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        }, "t1");
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 10;
        }, "t2");
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long end = System.currentTimeMillis();
        log.info("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }
}
