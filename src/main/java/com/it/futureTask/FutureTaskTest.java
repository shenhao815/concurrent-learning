package com.it.futureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
               System.out.println("running");
                Thread.sleep(2000);
                return 2000;
            }
        });

        Thread t1 = new Thread(task);
        t1.start();

        System.out.println(task.get());
    }
}
