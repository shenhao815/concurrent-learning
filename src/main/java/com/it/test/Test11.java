package com.it.test;

import com.it.patten.ProducerConsumer.Test;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j
public class Test11 {
    public static void main(String[] args) {
        Ele ele = new Ele();
        ClassLayout cl = ClassLayout.parseInstance(ele);
        new Thread(() -> {
            log.debug(cl.toPrintable());
            synchronized (ele) {
                log.debug(cl.toPrintable());
            }
            log.debug(cl.toPrintable());
            synchronized (Test11.class) {
                Test11.class.notifyAll();
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (Test11.class) {
                try {
                    Test11.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             }
            synchronized (ele) {

                log.debug(cl.toPrintable());
                log.debug(cl.toPrintable());
            }
            log.debug(cl.toPrintable());
        },"t1").start();
    }
}
class Ele{

}
