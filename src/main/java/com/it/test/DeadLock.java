package com.it.test;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLock {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();

        new Thread(() -> {
            synchronized (A) {
                log.debug("lock A");
                Sleeper.sleep(1000);
                synchronized (B) {
                    log.debug("lock B");
                    log.debug("操作");
                }
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (B) {
                log.debug("lock B");
                Sleeper.sleep(500);
                synchronized (A) {
                    log.debug("lock A");
                    log.debug("操作");
                }
            }
        },"t2").start();
    }
}