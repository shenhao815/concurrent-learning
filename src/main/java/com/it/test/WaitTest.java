package com.it.test;

import com.it.common.ThreadUtil;
import javafx.scene.layout.BorderImage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitTest {

    static final Object room = new Object();
    static boolean hasCigarette = false; // 没有烟
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.info("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.info("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                log.info("有烟没？【{}】", hasCigarette);
                if (hasCigarette) {
                    log.info("可以开始干活了");
                }
            }
        },"小南").start();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.info("可以开始干活了");
                }
            },"其它人").start();
        }
        ThreadUtil.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.info("烟到了噢");
                room.notify();
            }
        },"送烟的").start();
    }


}
