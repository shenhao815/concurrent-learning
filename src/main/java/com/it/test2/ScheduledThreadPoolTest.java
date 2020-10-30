package com.it.test2;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;

@Slf4j
public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        /*executor.schedule(() -> {
            log.debug("task1");
            int a = 1/0;
        }, 1, TimeUnit.SECONDS);
        executor.schedule(() -> {
            log.debug("task2...");
        }, 1, TimeUnit.SECONDS);*/

        log.debug("start...");
        /*executor.scheduleAtFixedRate(() -> {
            log.debug("running1...");
            Sleeper.sleep(2000);
        }, 1, 1, TimeUnit.SECONDS);

        executor.scheduleAtFixedRate(() -> {
            log.debug("running2...");
            Sleeper.sleep(2000);
        }, 1, 1, TimeUnit.SECONDS);*/

        /*executor.scheduleWithFixedDelay(() -> {
            log.debug("running...");
            Sleeper.sleep(2000);
        }, 1, 1, TimeUnit.SECONDS);*/

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future t = pool.submit(() -> {
                log.debug("task1");
                int i = 1/0;
                return true;
        });
        try {
            t.get();
        } catch (InterruptedException e) {
            log.info("1");
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.info("2");
            e.printStackTrace();
        }
    }
}
