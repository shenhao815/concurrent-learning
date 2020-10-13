package com.it.DIY.advanceThreadPool;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;

@Slf4j
public class TestSynchronousQueue {
    public static void main(String[] args) {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try{
                log.debug("putting {} ",1);
                integers.put(1);
                log.debug("{} putted",1);

                log.debug("putting {} ",2);
                integers.put(2);
                log.debug("{} putted",2);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        },"t1").start();

        new Thread(() -> {
            try {
                Integer i = integers.take();
                log.debug("taked {}",1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();

        new Thread(() -> {
            try {
                Integer i = integers.take();
                log.debug("taked {}",i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3").start();
    }

}
