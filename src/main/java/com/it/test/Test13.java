package com.it.test;

import com.it.common.Sleeper;

public class Test13 {
    static boolean run = true;
    public static void main(String[] args) {

        new Thread(() -> {
            while (run) {

            }
        }).start();
        Sleeper.sleep(1000);

        run = false;
    }
}
