package com.it.test;

/**
 * @author ch
 * @date 2020-9-24
 * @description
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        Thread.sleep(1500);
        t1.interrupt();
        System.out.println("打断状态："+t1.isInterrupted());
    }
}
