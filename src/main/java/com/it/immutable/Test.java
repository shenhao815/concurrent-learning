package com.it.immutable;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
public class Test {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    synchronized (sdf) {
                        log.debug("{}",sdf.parse("1951-04-21"));
                    }
                } catch (ParseException e) {
                    log.debug(e.getMessage());
                }
            }).start();
        }
    }
}
