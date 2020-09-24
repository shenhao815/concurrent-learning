package com.it.test;

public class Test12 {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                // 获取打断标记，该标记由别的线程调用interrupt()产生
                // 本线程根据打断标记来确定后续的操作
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    System.out.println("被打断了，我选择退出循环");
                    break;
                }

            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        System.out.println("interrupt");
        t1.interrupt(); // 只是通知要打断t1，而并没有真正的中断了t1的运行
    }
}
