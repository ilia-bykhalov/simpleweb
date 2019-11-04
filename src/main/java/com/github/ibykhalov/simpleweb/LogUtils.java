package com.github.ibykhalov.simpleweb;

import java.util.Date;

public final class LogUtils {
    private LogUtils() {
    }

    public static String timeDiff(Date start) {
        Date finish = new Date();
        long millis = finish.getTime() - start.getTime();
        return millis / 1000 + "." + millis % 1000;
    }
}
