package com.github.ibykhalov.simpleweb;

import com.github.ibykhalov.simpleweb.config.DatasourceConfig;
import com.github.ibykhalov.simpleweb.config.EmployeeServerConfig;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public final class TestUtils {
    private TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static final String TEST_SERVER_IP = "127.0.0.1";
    public static final int TEST_SERVER_PORT = 8888;

    public static final DatasourceConfig TEST_DATASOURCE_CONFIG =
            new DatasourceConfig("jdbc:postgresql://localhost:5400/simpleweb_test", "postgres", "docker", 1);
    public static final EmployeeServerConfig TEST_EMPLOYEE_SERVER_CONFIG =
            new EmployeeServerConfig(TEST_SERVER_PORT, 1, TEST_DATASOURCE_CONFIG);


    public static void assertXmlEquals(String expected, String actual) throws Exception {
        assertEquals(prettyAsXml(expected), prettyAsXml(actual));
    }

    private static String prettyAsXml(String input) throws Exception {
        Document xml = new SAXBuilder().build(new StringReader(input));
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter stringWriter = new StringWriter();
        xmlOutputter.output(xml, stringWriter);
        return stringWriter.toString();
    }

    public static String getFileText(String filePath) throws IOException {
        URL url = Resources.getResource(filePath);
        return Resources.toString(url, Charsets.UTF_8);
    }

    public static void truncateTable() {
        try (Connection connection = DriverManager.getConnection(TEST_DATASOURCE_CONFIG.getUrl(),
                                                                 TEST_DATASOURCE_CONFIG.getUser(),
                                                                 TEST_DATASOURCE_CONFIG.getPassword()
        )) {
            Statement statement = connection.createStatement();
            statement.execute("truncate table userinfo;");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
