package com.it.test;

import com.it.common.Sleeper;
import com.sun.javaws.ui.ApplicationIconGenerator;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j
public class Test10 {
    // static Pig pig = new Pig();
    // static ClassLayout classLayout = ClassLayout.parseInstance(pig);

    public static void main(String[] args) throws InterruptedException {
        Pig pig = new Pig();
        ClassLayout classLayout = ClassLayout.parseInstance(pig);
        log.debug("synchronized前");
        log.debug(classLayout.toPrintable(pig));
        Thread t1 = new Thread(() -> {
            synchronized (pig) {
                log.debug("synchronized中");
                log.debug(classLayout.toPrintable(pig));
                 //m2();
                // try {
                    //pig.wait();
                    Sleeper.sleep(2000);
                log.debug(classLayout.toPrintable(pig));
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }

            }
        },"t1");
        t1.start();
        Sleeper.sleep(3000);

        new Thread(() -> {
            System.out.println("==========>" + t1.isAlive());
            synchronized (pig) {
                log.debug("另一线程进入");
                log.debug(classLayout.toPrintable());
            }
        },"t2").start();

        Sleeper.sleep(7000);

        new Thread(() -> {
            synchronized (pig) {
                log.debug("t3线程进入");
                log.debug(classLayout.toPrintable());
            }
        },"t3").start();

        Sleeper.sleep(2000);
        new Thread(() -> {
            synchronized (pig) {
                log.debug("t4线程进入");
                log.debug(classLayout.toPrintable());
            }
        },"t4").start();
    }

    /*public static void m2(){
        synchronized (pig) {
            log.debug("方法2中。。。。。");
            log.debug(classLayout.toPrintable());
        }
    }*/
}

class Pig{

}
