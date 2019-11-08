package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.config.EmployeeServerConfig;
import com.github.ibykhalov.simpleweb.db.DataSource;
import com.github.ibykhalov.simpleweb.db.IUserInfoDAO;
import com.github.ibykhalov.simpleweb.db.UserInfoDAO;
import com.github.ibykhalov.simpleweb.webserver.HttpResponse;
import com.github.ibykhalov.simpleweb.webserver.IWebServer;
import com.github.ibykhalov.simpleweb.webserver.WebServer;
import com.github.ibykhalov.simpleweb.xml.XmlParser;
import com.github.ibykhalov.simpleweb.data.Request;
import com.github.ibykhalov.simpleweb.data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmployeeServer {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServer.class);

    private static final String UNKNOWN_ERROR_BODY_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?> <response> <result-code>2</result-code> </response>";

    private static final HttpResponse UNKNOWN_ERROR_HTTP_RESPONSE = new HttpResponse(200, UNKNOWN_ERROR_BODY_RESPONSE);

    private final IWebServer webServer;
    private final RequestProcessor requestProcessor;
    private final DataSource dataSource;

    public EmployeeServer(EmployeeServerConfig config) {
        webServer = new WebServer(config.getServerPort(), config.getWorkersCount(), this::handleRequest);
        dataSource = new DataSource(config.getDatasourceConfig());
        IUserInfoDAO dao = new UserInfoDAO(dataSource);
        requestProcessor = new RequestProcessor(dao);
    }

    private HttpResponse handleRequest(String requestBody) {
        try {
            Request request = XmlParser.parseRequest(requestBody);

            Response response = requestProcessor.process(request);

            String serialize = XmlParser.serialize(response);
            return new HttpResponse(200, serialize);
        } catch (Exception ex) {
            logger.error("unexpected error", ex);
            return UNKNOWN_ERROR_HTTP_RESPONSE;
        }
    }

    public void start() {
        webServer.start();
    }

    public void stop() {
        webServer.stop();
        dataSource.close();
    }
}
