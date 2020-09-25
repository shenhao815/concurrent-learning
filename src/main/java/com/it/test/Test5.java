package com.it.test;

import lombok.extern.slf4j.Slf4j;

import static com.it.common.ThreadUtil.sleep;


@Slf4j
public class Test5 {
    public static void main(String[] args) {
        log.debug("开始运行。。。");
        Thread t1 = new Thread(() -> {
            log.info("开始运行。。。");
            sleep(2000);
            log.info("运行结束...");
        }, "daemon");
        // 设置该线程为护线程
        t1.setDaemon(true);
        t1.start();

        sleep(1000);
        log.info("运行结束。。。");
    }
}
