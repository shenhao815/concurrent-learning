package com.it.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author ch
 * @date 2020-9-28
 * @description
 */
@Slf4j
public class BiasedTest {

    // 添加虚拟机参数 -XX：BiasedLockingStartupDelay=0
    public static void main(String[] args) {
        Dog d = new Dog();
        ClassLayout classLayout = ClassLayout.parseInstance(d);

        new Thread(() -> {
            log.info("synchronized前");
            System.out.println(classLayout.toPrintable());
            synchronized (d) {
                log.info("synchronized中");
                System.out.println(classLayout.toPrintable());
            }
            log.info("synchronized后");
            System.out.println(classLayout.toPrintable());
        },"t1").start();
    }
}
class Dog{

}
