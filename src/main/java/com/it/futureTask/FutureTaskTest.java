package com.it.futureTask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                log.info("running");
                Thread.sleep(2000);
                return 2000;
            }
        });

        // 参数1是任务对象；参数2是线程名字，推荐
        Thread t1 = new Thread(task,"t1");
        t1.start();
        // 主线程阻塞，同步等待task执行完毕的结果
        Integer result = task.get();

        log.info(result.toString());
    }
}
