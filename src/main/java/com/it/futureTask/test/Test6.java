package com.it.futureTask.test;

public class Test6 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();

        System.out.println("t1 state：" + t1.getState());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("t1 state 2: " + t1.getState());

        Thread.sleep(3000);
        System.out.println("t1 state 3: " + t1.getState());
    }
}
