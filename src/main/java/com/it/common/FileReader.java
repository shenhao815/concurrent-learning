package com.it.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class FileReader {
    public static void read(String filename) {
        int idx = filename.lastIndexOf(File.separator);
        String shortName = filename.substring(idx + 1);
        try {
            FileInputStream in = new FileInputStream(filename);
            final long start = System.currentTimeMillis();
            log.info("read [{}] start ...",shortName);
            byte[] buf = new byte[1024];
            int n = -1;
            do {
                n = in.read(buf);
            } while (n != -1);
            final long end = System.currentTimeMillis();
            log.info("read [{}] end ... cost: {} ms",shortName,end - start);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
