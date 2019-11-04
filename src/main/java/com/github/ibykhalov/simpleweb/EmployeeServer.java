package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.webserver.IWebServer;
import com.github.ibykhalov.simpleweb.webserver.Response;
import com.github.ibykhalov.simpleweb.webserver.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmployeeServer {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServer.class);

    private final IWebServer webServer;

    public EmployeeServer(int port) {
        webServer = new WebServer(port, this::handle);
    }

    private Response handle(String reqest) {
        return new Response(200, "");
    }

    public void start() {
        webServer.start();
    }


    public void stop() {
        webServer.stop();
    }
}
