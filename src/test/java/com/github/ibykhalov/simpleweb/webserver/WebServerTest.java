package com.github.ibykhalov.simpleweb.webserver;

import com.github.ibykhalov.simpleweb.HttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.ibykhalov.simpleweb.TestUtils.TEST_SERVER_IP;
import static com.github.ibykhalov.simpleweb.TestUtils.TEST_SERVER_PORT;
import static org.junit.Assert.assertEquals;

public class WebServerTest {
    private static final String REQUEST_1 = "test request 1";
    private static final HttpResponse RESPONSE_1 = new HttpResponse(200, "test response 1");
    private static final String REQUEST_2 = "test request 2";
    private static final HttpResponse RESPONSE_2 = new HttpResponse(500, "test response 2");

    @Test
    public void shouldUseHandler() throws IOException {
        assertServerUserHandler(REQUEST_1, RESPONSE_1);
        assertServerUserHandler(REQUEST_2, RESPONSE_2);
    }

    private static void assertServerUserHandler(String expectedRequest, HttpResponse expectedResponse)
            throws IOException {
        AtomicReference<String> actualRequest = new AtomicReference<>();
        WebServer webServer = new WebServer(TEST_SERVER_PORT, 1, request -> {
            actualRequest.set(request);
            return expectedResponse;
        });
        try {
            webServer.start();

            HttpClient httpClient = new HttpClient();
            HttpResponse actualResponse = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, expectedRequest);

            assertEquals(expectedRequest, actualRequest.get());
            assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
            assertEquals(expectedResponse.getBody(), actualResponse.getBody());
        } finally {
            webServer.stop();
        }
    }
}
