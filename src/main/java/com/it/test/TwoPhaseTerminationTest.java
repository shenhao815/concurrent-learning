package com.it.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test")
public class TwoPhaseTerminationTest {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        Thread.sleep(3500);
        tpt.stop();
    }
}
class TwoPhaseTermination{
    private Thread monitor;

    // 启动监控线程
    public void start(){
        monitor = new Thread(() -> {
            while (true) {
                if (monitor.isInterrupted()) {
                    System.out.println("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000); // 第一处被中断(sleep中)
                    System.out.println("执行监控中，是一个有过程的操作"); // 第二处被中断(正常运行中)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 重新设置打断标记  此处很重要，不可省略
                    monitor.interrupt();
                }
            }
        }, "monitor");
        monitor.start();
    }

    // 停止监控线程
    public void stop(){
        monitor.interrupt();
    }
}
