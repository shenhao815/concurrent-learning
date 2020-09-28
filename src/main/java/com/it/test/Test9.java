package com.it.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;

/**
 * @author ch
 * @date 2020-9-28
 * @description
 */
@Slf4j
public class Test9 {

    public static void main(String[] args){
        Cat c = new Cat();
        Thread t1 = new Thread(() -> {
            synchronized (c) {
                log.info(ClassLayout.parseInstance(c).toPrintable());
            }
            synchronized (Test9.class) {
                Test9.class.notify();
            }
            // 如果不用wait/notify使用 join必须打开下面的注释
            // 因为：t1线程不能结束，否则底层线程可能被jvm重用作为t2线程，底层线程id是一样的
            /*try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }, "t1");
        t1.start();
        Thread t2 = new Thread(() -> {
            synchronized (Test9.class) {
                try {
                    Test9.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info(ClassLayout.parseInstance(c).toPrintable());
            synchronized (c) {
                log.info(ClassLayout.parseInstance(c).toPrintable());
            }
            log.info(ClassLayout.parseInstance(c).toPrintable());
        }, "t2");
        t2.start();
    }
}
class Cat{

}
