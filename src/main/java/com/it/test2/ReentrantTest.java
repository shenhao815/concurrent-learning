package com.it.test2;

import com.it.common.Sleeper;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantTest {
    static volatile SReentrantLock sReentrantLock = new SReentrantLock(5);

    public static void main(String[] args) {
        Condition aSet = sReentrantLock.newCondition();
        Condition bSet = sReentrantLock.newCondition();
        Condition cSet = sReentrantLock.newCondition();

        Thread t1 = new Thread(() -> {
            sReentrantLock.print("a",aSet,bSet);
        });
        Thread t2 = new Thread(() -> {
            sReentrantLock.print("b",bSet,cSet);
        });
        Thread t3 = new Thread(() -> {
            sReentrantLock.print("c",cSet,aSet);
        });
        // Sleeper.sleep(1000);
        t1.start();
        t2.start();
        t3.start();
        sReentrantLock.start(aSet);
    }
}

class SReentrantLock extends ReentrantLock {
    private int loopNumber;

    public SReentrantLock(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition currentCond, Condition nextCond) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try{
                currentCond.await();
                System.out.println(str);
                nextCond.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                this.unlock();
            }
        }
    }

    public void start(Condition first){
        this.lock();
        try{
            first.signal();
        }finally{
            this.unlock();
        }
    }
}
