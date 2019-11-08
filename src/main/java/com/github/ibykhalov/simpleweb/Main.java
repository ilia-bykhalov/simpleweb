package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.config.ConfigLoader;
import com.github.ibykhalov.simpleweb.config.EmployeeServerConfig;
import com.github.ibykhalov.simpleweb.core.EmployeeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public final class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        logger.info("starting with args" + Arrays.toString(args));

        EmployeeServerConfig employeeServerConfig = ConfigLoader.fromArgs(args);
        EmployeeServer employeeServer = new EmployeeServer(employeeServerConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
            employeeServer.stop();
            logger.info("Server stopped.");
        }));

        employeeServer.start();
    }
}
