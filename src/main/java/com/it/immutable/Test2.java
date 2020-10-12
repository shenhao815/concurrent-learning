package com.it.immutable;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Test2 {

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                LocalDate date = dtf.parse("2018-10-01",LocalDate::from);
                log.debug("{}",date);
            }).start();
        }
    }
}
