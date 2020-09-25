package com.it.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ch
 * @date 2020-9-25
 * @description join方法测试
 */
@Slf4j
public class Test2 {

    static int r = 0;

    public static void main(String[] args) {
        test1();
    }
    private static void test1() {
      log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("结束");
            r = 10;
        });
        t1.start();
        log.debug("r的结果为：{}", r);
        log.debug("结束");
    }
}
