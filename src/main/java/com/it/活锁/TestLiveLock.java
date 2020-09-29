package com.it.活锁;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLiveLock {
    static volatile int counter = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while (counter>0) {
                Sleeper.sleep(200);
                counter--;
                log.debug("counter: {}",counter);
            }
        },"t1").start();
        new Thread(() -> {
            while (counter<20) {
                Sleeper.sleep(200);
                counter++;
                log.debug("counter: {}",counter);
            }
        },"t2").start();
    }
}
