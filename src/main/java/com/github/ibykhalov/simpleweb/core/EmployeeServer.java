package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.db.Database;
import com.github.ibykhalov.simpleweb.webserver.IWebServer;
import com.github.ibykhalov.simpleweb.webserver.Response;
import com.github.ibykhalov.simpleweb.webserver.WebServer;
import com.github.ibykhalov.simpleweb.xml.XmlParser;
import com.github.ibykhalov.simpleweb.xml.data.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmployeeServer {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServer.class);

    private static final String UNKNOWN_ERROR_BODY_RESPONCE =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?> <response> <result-code>2</result-code> </response>";

    private final IWebServer webServer;

    private final IProcessor processor = new Processor(new Database());

    public EmployeeServer(int port) {
        webServer = new WebServer(port, this::handle);
    }

    private Response handle(String reqest) {
        try {
            Request request = XmlParser.parseRequest(reqest);
            com.github.ibykhalov.simpleweb.xml.data.Response response = processor.processRequest(request);
            String serialize = XmlParser.serialize(response);
            return new Response(200, serialize);
        } catch (Exception ex) {
            logger.error("unexcpected error", ex);
            return new Response(200, UNKNOWN_ERROR_BODY_RESPONCE);
        }
    }

    public void start() {
        webServer.start();
    }


    public void stop() {
        webServer.stop();
    }
}
