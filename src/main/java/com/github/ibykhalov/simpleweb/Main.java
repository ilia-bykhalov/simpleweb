package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.core.EmployeeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;

import static com.github.ibykhalov.simpleweb.LogUtils.timeDiff;


public final class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        logger.info("starting with args" + Arrays.toString(args));
        Date start = new Date();
        new EmployeeServer(8888).start();

        logger.info("finished in " + timeDiff(start));
    }
}
