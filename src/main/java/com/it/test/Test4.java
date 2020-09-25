package com.it.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ch
 * @date 2020-9-25
 * @description
 */
@Slf4j
public class Test4 {
    static int r1 = 0;
    static int r2 = 0;
    public static void main(String[] args) throws InterruptedException {
        test3();
    }
    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        }, "t1");
        long start = System.currentTimeMillis();
        t1.start();
        t1.join(1500);
        long end = System.currentTimeMillis();
        log.info("r1:{} r2:{} cost:{}", r1, r2, end - start);
    }
}
