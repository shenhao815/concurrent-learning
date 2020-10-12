package com.it.atomic;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LockCas {

    private AtomicInteger state = new AtomicInteger(0);

    public void lock(){
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }
    public void unlock(){
        log.debug("unlock...");
        state.set(0);
    }

    public static void main(String[] args) {
        LockCas lockCas = new LockCas();
        new Thread(() -> {
            log.debug("begin");
            lockCas.lock();
            try{
                log.debug("lock...");
                Sleeper.sleep(1000);
            }finally{
                lockCas.unlock();
            }
        }).start();
        new Thread(() -> {
            log.debug("begin");
            lockCas.lock();
            try{
                log.debug("lock...");
            }finally{
                lockCas.unlock();
            }
        }).start();
    }
}
