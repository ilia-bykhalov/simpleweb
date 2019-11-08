package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.webserver.Response;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public Response doPost(String ip, int port, String message) throws IOException {
        HttpPost request = new HttpPost("http://" + ip + ":" + port);
        HttpEntity requestEntity = new StringEntity(message);
        request.setEntity(requestEntity);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            logger.info(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();

            String result = IOUtils.toString(entity.getContent());
            logger.info("result" + result);
            return new Response(response.getStatusLine().getStatusCode(), result);
        }
    }
}