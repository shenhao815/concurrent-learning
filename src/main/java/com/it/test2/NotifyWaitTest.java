package com.it.test2;

public class NotifyWaitTest {
    public static void main(String[] args) {
        SNotifyWait sNotifyWait = new SNotifyWait(5, 1);
        new Thread(() -> {
            sNotifyWait.print("a",1,2);
        }, "t1").start();
        new Thread(() -> {
            sNotifyWait.print("b",2,3);
        }, "t2").start();
        new Thread(() -> {
            sNotifyWait.print("c",3,1);
        }, "t3").start();
    }
}

class SNotifyWait {
    private int loopNumber;
    private final Object lock = new Object();
    private int flag;

    public SNotifyWait(int loopNumber, int flag) {
        this.loopNumber = loopNumber;
        this.flag = flag;
    }

    public void print(String str, int currentFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (lock) {
                while (this.flag != currentFlag) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(str);
                this.flag = nextFlag;
                lock.notifyAll();
            }
        }
    }
}
