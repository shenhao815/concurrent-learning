package com.it.test;

public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

        }, "t1");

        t1.start();

        Thread.sleep(1000);
    }
}
