package com.it.reentrantLock;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ch
 * @date 2020-9-30
 * <p>
 * 多个条件变量
 */
@Slf4j
public class Test2 {
    static ReentrantLock room = new ReentrantLock();
    static volatile boolean hasCigarette = false;
    static volatile boolean hasTakeout = false;
    static Condition cigAwaitSet = room.newCondition();
    static Condition takeAwaitSet = room.newCondition();
    public static void main(String[] args) {
        new Thread(() -> {
            try{
                room.lock();
                while (!hasCigarette) {
                    try {
                        log.debug("没烟，不干活");
                        cigAwaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("开始干活");
            }finally{
                room.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            try{
                room.lock();
                while (!hasTakeout) {
                    try {
                        log.debug("没外卖，不干活");
                        takeAwaitSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("开始干活");
            }finally{
                room.unlock();
            }
        }, "小女").start();

        Sleeper.sleep(1000);
        sendCigarette();
        Sleeper.sleep(1000);
        sendTakeout();
    }

    public static void sendCigarette(){
        room.lock();
        try{
            log.debug("送烟来了");
            hasCigarette = true;
            cigAwaitSet.signalAll();

        }finally{
            room.unlock();
        }
    }
    public static void sendTakeout(){
        room.lock();
        try{
            log.debug("送外卖来了");
            hasTakeout = true;
            takeAwaitSet.signalAll();
        }finally{
            room.unlock();
        }
    }
}
