package com.it.test;

import lombok.extern.slf4j.Slf4j;

import static com.it.common.Sleeper.sleep;

@Slf4j
public class ThreadStateTest {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {log.info("running...");}
        };

        final Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) { }// runnable
            }
        };
        t2.start();

        final Thread t3 = new Thread("t3") {
            @Override
            public void run() {log.info("running...");} // terminated
        };
        t3.start();

        final Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (ThreadStateTest.class) {
                    try {
                        Thread.sleep(1000000); // timed_waiting (有时限的等待)
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        final Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join(); // waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        final Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (ThreadStateTest.class) {
                    try {
                        Thread.sleep(1000000); // blocked
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        sleep(500);
        log.info("t1 state {}",t1.getState());
        log.info("t2 state {}",t2.getState());
        log.info("t3 state {}",t3.getState());
        log.info("t4 state {}",t4.getState());
        log.info("t5 state {}",t5.getState());
        log.info("t6 state {}",t6.getState());

    }
}
