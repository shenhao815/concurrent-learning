package com.it.reentrantLock;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;


/**
 * @author ch
 * @date 2020-9-30
 *
 * ReentrantLock 可打断验证
 */
@Slf4j
public class Interruptibly {
   static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        final Thread t1 = new Thread(() -> {
            log.debug("启动。。。");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("等锁的过程中被打断");
                return;
            }
            try {
                log.debug("获得了锁");
            } catch (Exception ex) {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try{
            Sleeper.sleep(1000);
            t1.interrupt();
            log.debug("执行被打断");
        }finally{
            lock.unlock();
        }
    }

}
