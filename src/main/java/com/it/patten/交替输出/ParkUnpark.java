package com.it.patten.交替输出;

import java.util.concurrent.locks.LockSupport;

public class ParkUnpark {
    public static void main(String[] args) {
        SyncParkUnpark syncParkUnpark = new SyncParkUnpark(5);
        final Thread t1 = new Thread(() -> {
            syncParkUnpark.print("a");
        }, "t1");
        final Thread t2 = new Thread(() -> {
            syncParkUnpark.print("b");
        }, "t2");
        final Thread t3 = new Thread(() -> {
            syncParkUnpark.print("c\n");
        }, "t3");
        syncParkUnpark.setThreads(t1,t2,t3);
        syncParkUnpark.start();
    }
}
class SyncParkUnpark{
    private int loopNumber;
    private Thread[] threads;

    public SyncParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void setThreads(Thread... threads) {
        this.threads = threads;
    }

    public void print(String string) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(string);
            LockSupport.unpark(nextThread());
        }
    }

    private Thread nextThread() {
        Thread current = Thread.currentThread();
        int index = 0;

        for (int i = 0; i < threads.length; i++) {
            if (threads[i] == current) {
                index = i;
                break;
            }
        }
        if (index < threads.length - 1) {
            return threads[index + 1];
        } else {
            return threads[0];
        }
    }

    public void start(){
        for (Thread thread : threads) {
            thread.start();
        }
        LockSupport.unpark(threads[0]);
    }
}
