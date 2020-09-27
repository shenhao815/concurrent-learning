package com.it.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test17 {
    static int counter = 0;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            for (int i = 0; i < 5000; i++) {
                //synchronized (lock) {
                    counter ++;
                    //log.info(counter+"");
//                }
            }

        },"t1");
        Thread t2 = new Thread(() ->{
            for (int i = 0; i < 5000; i++) {
//                synchronized (lock) {
                    counter--;
                    //log.info(counter+"");
//                }
            }
        },"t2");
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        log.info(counter+"");
    }
}
