package com.it.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestSchedule {

    // 如何让每周四 18:00:00 定时执行任务
    public static void main(String[] args) {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取周四的时间
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);

        // 如果 当前时间> 本周周四，必须找到下周周四
        if (now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }

        // initialDelay 代表当前时间和周四的时间差
        // period 一周的间隔时间
        long initialDelay = Duration.between(now, time).toMillis();
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            System.out.println("running...");
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
