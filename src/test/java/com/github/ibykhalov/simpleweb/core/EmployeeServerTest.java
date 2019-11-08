package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.HttpClient;
import com.github.ibykhalov.simpleweb.TestUtils;
import com.github.ibykhalov.simpleweb.webserver.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.github.ibykhalov.simpleweb.TestUtils.*;
import static org.junit.Assert.assertEquals;

public class EmployeeServerTest {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServerTest.class);

    private final HttpClient httpClient = new HttpClient();
    private EmployeeServer server;

    @Before
    public void setUp() throws Exception {
        server = new EmployeeServer(TEST_EMPLOYEE_SERVER_CONFIG);
        server.start();
        logger.info("server starts");
        TestUtils.truncateTable();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void register() throws Exception {
        HttpResponse result = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("register_request"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("register_request_response_ok"), result.getBody());
    }

    @Test
    public void registerTwice() throws Exception {
        httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("register_request"));
        HttpResponse result = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("register_request"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("register_request_response_error"), result.getBody());
    }

    @Test
    public void getBalance() throws Exception {
        httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("register_request"));

        HttpResponse result = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("get_balance_request"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("get_balance_request_response"), result.getBody());
    }

    @Test
    public void getBalanceOnUserNotFound() throws Exception {
        HttpResponse result =
                httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("get_balance_user_not_found"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("get_balance_user_not_found_response"), result.getBody());
    }

    @Test
    public void getBalanceOnPasswordIncorrect() throws Exception {
        httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("register_request"));

        HttpResponse result =
                httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("get_balance_password_incorrect"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("get_balance_password_incorrect_response"), result.getBody());
    }

    @Test
    public void badRequestType() throws Exception {
        HttpResponse result = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("bad_request_type"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("bad_request_type_response"), result.getBody());
    }


    @Test
    public void badRequestXml() throws Exception {
        HttpResponse result = httpClient.doPost(TEST_SERVER_IP, TEST_SERVER_PORT, getTestString("bad_request_xml"));

        assertEquals(200, result.getStatus());
        assertXmlEquals(getTestString("bad_request_xml_response"), result.getBody());
    }

    private String getTestString(String fileName) throws IOException {
        return getFileText("employeeservertest/" + fileName + ".xml");
    }
}
