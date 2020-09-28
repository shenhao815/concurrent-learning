package com.it.test;

/**
 * @author ch
 * @date 2020-9-27
 * @description
 */
public class Test7 {

    static final Object lock = new Object();
    static int counter = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            counter++;
        }
    }
}
