package com.it.patten.guardedObject;

import com.it.common.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author ch
 * @date 2020-9-29
 * @description
 *
 * 同步模式之保护性暂停
 * 定义：
 * 即Guarded Suspension,用在一个线程等待另一个线程的执行结果
 * 要点：
 *  有一个结果需要从一个线程传递到另一个线程，让他们关联同一个GuardedObject
 *  如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 *  JDK中，join的实现、Future的实现，采用的就是此模式
 *  因为要等待另一方的结果，因此归类到同步模式
 */
@Slf4j
public class GuardedSingle {

    public static void main(String[] args) {

        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            ThreadUtil.sleep(1000);
            guardedObject.complete(null);
            ThreadUtil.sleep(1000);
            guardedObject.complete(Arrays.asList("a","b","c"));
        },"下载任务").start();

        Object res = guardedObject.get(2500);
        if (res != null) {
            log.info("get response:[{}] lines",((List<String>)res).size());
        }
    }
}
@Slf4j
class GuardedObject{
    private final Object lock = new Object();
    private Object response;

    public Object get(long millis){
        synchronized (lock) {
            // 记录最初时间
            long begin = System.currentTimeMillis();
            // 已经经历的时间
            long passTime = 0;
            while (response == null) {
                // 假设millis是1000，结果在400时唤醒了，那么还有600ms要等
                long waitTime = millis-passTime;
                if (waitTime <= 0) {
                    log.debug("break...");
                    break;
                }
                try {
                    lock.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 如果提前被唤醒，这时已经经历的时间假设为 400ms
                passTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等线程
            this.response = response;
            log.debug("notify...");
            lock.notifyAll();
        }
    }
}


