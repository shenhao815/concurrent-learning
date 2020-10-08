package com.it.test2;

import com.it.common.Sleeper;

import java.util.concurrent.locks.LockSupport;

public class ParkUnparkTest {
    public static void main(String[] args) {
        SParkUnpark sParkUnpark = new SParkUnpark(5);
        final Thread t1 = new Thread(() -> {
            sParkUnpark.print("a");
        }, "t1");
        final Thread t2 = new Thread(() -> {
            sParkUnpark.print("b");
        }, "t2");
        final Thread t3 = new Thread(() -> {
            sParkUnpark.print("c");
        }, "t3");
        t1.start();
        t2.start();
        t3.start();
        Sleeper.sleep(1000);
        sParkUnpark.setThreads(t1,t2,t3);
        sParkUnpark.start();
    }
}
class SParkUnpark{
    private Thread[] threads;
    private int loopNumber;

    public SParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void setThreads(Thread...threads) {
        this.threads = threads;
    }

    public void print(String str) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(nextThread());
        }
    }

    private Thread nextThread() {
        Thread currentThread = Thread.currentThread();
        for (int i = 0; i < threads.length-1; i++) {
            if (currentThread == threads[i]) {
                return threads[i + 1];
            }
        }
        return threads[0];
    }

    public void start(){
        LockSupport.unpark(threads[0]);
    }
}
