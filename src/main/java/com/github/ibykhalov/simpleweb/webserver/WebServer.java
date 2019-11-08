package com.github.ibykhalov.simpleweb.webserver;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer implements IWebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private final int port;
    private final IRequestHandler requestHandler;

    public WebServer(int port, IRequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
    }

    private boolean isStopped = false;

    @Override
    public void start() {
        ServerSocket server = startSocketServer();

        new Thread(() -> {
            try {

                while (!isStopped) {
                    try {
                        logger.info("listen");
                        Socket socket = server.accept();
                        logger.info("accepted");

                        new Thread(() -> processSocket2(socket)).start();
                    } catch (SocketTimeoutException soex) {
                        logger.info("timeout");
                    }
                }

                server.close();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }).start();
    }

    private ServerSocket startSocketServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(100);
            logger.info("Listening for connection on port " + port + " ....");
            return server;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

     private void processSocket2(Socket socket) {
        try {
            DefaultBHttpServerConnection connection =
                    DefaultBHttpServerConnectionFactory.INSTANCE.createConnection(socket);
            HttpRequest httpRequest = connection.receiveRequestHeader();
            connection.receiveRequestEntity((HttpEntityEnclosingRequest)httpRequest);
            HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
            String reqestString = IOUtils.toString(entity.getContent());
            logger.info("reqeust="+reqestString);

            Response response = requestHandler.execRequest(reqestString);

            BasicStatusLine d = new BasicStatusLine(HttpVersion.HTTP_1_1,response.getStatus(),null);
            HttpResponse httpResponse = DefaultHttpResponseFactory.INSTANCE.newHttpResponse(d, null);
            StringEntity d1 = new StringEntity(response.getBody());
            httpResponse.setEntity(d1);
            connection.sendResponseHeader(httpResponse);
            connection.sendResponseEntity(httpResponse);

            connection.flush();

            logger.info("close");

            socket.close();
        } catch (Exception ex) {
            logger.error("weqwe", ex);
        }
     }

    @Override
    public void stop() {
        try {
            isStopped = true;

            Thread.sleep(200);
            //TODO thread wait
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}