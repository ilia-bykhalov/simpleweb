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
    private static final Response RESPONSE_1 = new Response(200, "test response 1");
    private static final String REQUEST_2 = "test request 2";
    private static final Response RESPONSE_2 = new Response(500, "test response 2");

    @Test
    public void shouldUseHandler() throws IOException {
        assertServerUserHandler(REQUEST_1, RESPONSE_1);
        assertServerUserHandler(REQUEST_2, RESPONSE_2);
    }

    private static void assertServerUserHandler(String expectedRequest, Response expectedResponse) throws IOException {
        AtomicReference<String> actualRequest = new AtomicReference<>();
        WebServer webServer = new WebServer(TEST_SERVER_PORT, request -> {
            actualRequest.set(request);
            return expectedResponse;
        });
        try {
            webServer.start();

            HttpClient httpClient = new HttpClient();
            Response actualResponse = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, expectedRequest);

            assertEquals(expectedResponse, actualResponse);
            assertEquals(expectedRequest, actualRequest.get());
        } finally {
            webServer.stop();
        }
    }
}
