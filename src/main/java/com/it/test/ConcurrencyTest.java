package com.it.test;


import com.it.common.Sleeper;

public class ConcurrencyTest {
    static int x;

    public static void main(String[] args){
        Thread t2 = new Thread(()->{
            while(true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(x);
                    break;
                }
            }
        },"t2");
        t2.start();

        new Thread(()->{
            Sleeper.sleep(1000);
            x = 10;
            t2.interrupt();
        },"t1").start();

        while(!t2.isInterrupted()){
            Thread.yield();
        }
        System.out.println(x);
    }
}
