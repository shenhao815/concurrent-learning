package com.it.common;

public class Sleeper {

    public static void sleep(long n){
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
