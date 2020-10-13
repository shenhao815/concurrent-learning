package com.it.DIY;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TestSubmit {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        /*Future<String> future = pool.submit(() -> {
            log.debug("running");
            return "ok";
        });*/
        Future<String> future = pool.submit(() -> {
            log.debug("running");
            return "ok";
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
