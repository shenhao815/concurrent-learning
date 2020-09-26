package com.it.test;

import static com.it.common.ThreadUtil.sleep;

public class Test6 {

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (Test6.class) {
                sleep(100);
                System.out.println("1");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (Test6.class) {
                sleep(100);
                System.out.println("2");
            }
        }, "t2").start();
        new Thread(() -> {
            synchronized (Test6.class) {
                sleep(100);
                System.out.println("3");
            }
        }, "t3").start();

    }
}
