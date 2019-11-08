package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.db.Database;
import com.github.ibykhalov.simpleweb.webserver.Response;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

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
        new Database().truncate();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void shouldAnswer() throws Exception {
        Response result = httpClient.doPost(defaultIp, defaultPort, getFileText("register_request"));

        assertEquals(200, result.getStatus());
        assertEquals(prettyAsXml(getFileText("register_response_ok")), prettyAsXml(result.getBody()));
    }

    @Test
    public void shouldReturnZeroBalanceForRegisteredClient() throws Exception {
        httpClient.doPost(defaultIp, defaultPort, getFileText("register_request"));

        Response result = httpClient.doPost(defaultIp, defaultPort,
                                            getFileText("get_balance_request")
        );

        assertEquals(200, result.getStatus());
        assertEquals(prettyAsXml(getFileText("get_balance_response_ok")), prettyAsXml(result.getBody()));

    }

    private String getFileText(String fileName) throws IOException {
        URL url = Resources.getResource("employeeservertest/" + fileName + ".xml");
        return Resources.toString(url, Charsets.UTF_8);
    }

    private static String prettyAsXml(String input) throws Exception {
        Document xml = new SAXBuilder().build(new StringReader(input));
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter stringWriter = new StringWriter();
        xmlOutputter.output(xml, stringWriter);
        return stringWriter.toString();
    }
}
