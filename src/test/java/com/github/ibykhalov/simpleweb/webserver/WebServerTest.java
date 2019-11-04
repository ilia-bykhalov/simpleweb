package com.github.ibykhalov.simpleweb.webserver;

import com.github.ibykhalov.simpleweb.HttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class WebServerTest {

    private final String request1 = "test request 1";
    private final Response response1 = new Response(200, "test response 1");
    private final String request2 = "test request 2";
    private final Response response2 = new Response(500, "test response 2");

    @Test
    public void shouldUseHandler() throws IOException {
        assertServerUserHandler(request1, response1);
        assertServerUserHandler(request2, response2);
    }

    private static void assertServerUserHandler(String expectedRequest, Response expectedResponse) throws IOException {
        AtomicReference<String> actualRequest = new AtomicReference<>();
        WebServer webServer = new WebServer(8888, request -> {
            actualRequest.set(request);
            return expectedResponse;
        });
        try {
            webServer.start();

            HttpClient httpClient = new HttpClient();
            Response actualResponse = httpClient.doPost("127.0.0.1", 8888, expectedRequest);

            assertEquals(expectedResponse, actualResponse);
            assertEquals(expectedRequest, actualRequest.get());
        } finally {
            webServer.stop();
        }
    }
}