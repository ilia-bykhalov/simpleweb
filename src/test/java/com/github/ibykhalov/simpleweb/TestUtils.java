package com.github.ibykhalov.simpleweb;

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

import static org.junit.Assert.assertEquals;

public final class TestUtils {
    private TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static final String TEST_SERVER_IP = "127.0.0.1";
    public static final int TEST_SERVER_PORT = 8888;


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
}
