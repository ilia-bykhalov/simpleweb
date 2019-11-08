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

import java.io.IOException;
import java.net.Socket;

public final class SocketProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SocketProcessor.class);

    private final Socket socket;
    private final IRequestHandler requestHandler;

    public SocketProcessor(Socket socket, IRequestHandler requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try {
            DefaultBHttpServerConnection connection =
                    DefaultBHttpServerConnectionFactory.INSTANCE.createConnection(socket);

            String requestBody = getRequestBody(connection);

            HttpResponse response = requestHandler.execRequest(requestBody);

            writeResponseToSocket(connection, response);

            logger.debug("close");
            socket.close();
        } catch (HttpException | IOException ex) {
            logger.error("Cant transfer data", ex);
        } catch (Exception ex) {
            logger.error("Unexpected Exception", ex);
        }
    }

    private void writeResponseToSocket(DefaultBHttpServerConnection connection, HttpResponse response)
            throws HttpException, IOException {
        BasicStatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, response.getStatus(), null);

        org.apache.http.HttpResponse httpResponse =
                DefaultHttpResponseFactory.INSTANCE.newHttpResponse(statusLine, null);

        StringEntity stringEntity = new StringEntity(response.getBody());
        httpResponse.setEntity(stringEntity);

        connection.sendResponseHeader(httpResponse);
        connection.sendResponseEntity(httpResponse);

        connection.flush();
    }

    private String getRequestBody(DefaultBHttpServerConnection connection) throws HttpException, IOException {
        HttpRequest httpRequest = connection.receiveRequestHeader();
        connection.receiveRequestEntity((HttpEntityEnclosingRequest) httpRequest);
        HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();

        String requestString = IOUtils.toString(entity.getContent());
        logger.debug("request=" + requestString);
        return requestString;
    }
}
