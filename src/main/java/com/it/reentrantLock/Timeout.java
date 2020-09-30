package com.it.reentrantLock;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author ch
 * @date 2020-9-30
 * <p>
 * ReentrantLock 锁超时验证
 */
@Slf4j
public class Timeout {
    static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        final Thread t1 = new Thread(() -> {
            log.debug("启动。。。");
            boolean isGogLock = false;
            try {
                if (! (isGogLock = lock.tryLock(2000, TimeUnit.MILLISECONDS))) {
                    log.debug("没获得到锁");
                    return;
                }
                isGogLock = true;
                log.debug("获得到锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                if(isGogLock)
                lock.unlock();
            }
        }, "t1");

        t1.start();
        lock.lock();
        Sleeper.sleep(3000);
        lock.unlock();
    }

}
