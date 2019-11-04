package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.webserver.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class EmployeeServerTest {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServerTest.class);


    private static final String defaultIp = "127.0.0.1";
    private static final int defaultPort = 8888;


    private final HttpClient httpClient = new HttpClient();
    private EmployeeServer server;

    @Before
    public void setUp() throws Exception {

        server = new EmployeeServer(defaultPort);
        server.start();
        logger.info("serrver starts");
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldAnswer() throws Exception {


        Response result = httpClient.doPost(defaultIp, defaultPort,
                                                       "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<request>\n" +
                                                       "<request-type>CREATE-AGT</request-type>\n" +
                                                       "<extra name=\"login\">123456</extra>\n" +
                                                       "<extra name=\"password\">pwd</extra>\n" + "</request>"
        );

        assertEquals(200, result.getStatus());
        assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?><response><result-code>0</result-code></response>",
                     result.getBody()
        );
    }

    @Test
    public void shouldReturnZeroBalanceForRegisteredClient() throws Exception {
        shouldAnswer();

        Response result = httpClient.doPost(defaultIp, defaultPort,
                                                       "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<request>\n" +
                                                       "<request-type>GET-BALANCE</request-type>\n" +
                                                       "<extra name=\"login\">123456</extra>\n" +
                                                       "<extra name=\"password\">pwd</extra>\n" + "</request>"
        );

        String expectedBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<response>\n" +
                              "<result-code>0</result-code>\n" + "<extra name=\"balance\">100.00</extra>\n" +
                              "</response>";
        assertEquals(200, result.getStatus());
        assertEquals(expectedBody, result.getBody());

    }
}
