package com.it.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;


/**
 * @author ch
 * @date 2020-9-30
 *
 * ReentrantLock 可重入验证
 */
@Slf4j
public class Test1 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        m1();
    }

    public static void  m1(){
        try{
            lock.lock();
            log.debug("enter m1");
            m2();
        }finally {
            lock.unlock();
        }
    }

    private static void m2() {
        try{
            lock.lock();
            log.debug("enter m2");
            m3();
        }finally {
            lock.unlock();
        }
    }

    private static void m3() {
        try{
            lock.lock();
            log.debug("enter m3");
        }finally{
            lock.unlock();
        }
    }
}
