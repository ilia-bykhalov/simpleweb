package com.github.ibykhalov.simpleweb.xml;

import com.github.ibykhalov.simpleweb.exception.XmlParsingException;
import com.github.ibykhalov.simpleweb.xml.data.Request;
import com.github.ibykhalov.simpleweb.xml.data.RequestType;
import com.github.ibykhalov.simpleweb.xml.data.Response;
import com.github.ibykhalov.simpleweb.xml.data.ResponseCode;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class XmlParserTest {
    @Test
    public void parseRegisterRequest() throws Exception {
        String registerRequest = getFileText("register_request");

        Request request = XmlParser.parseRequest(registerRequest);

        assertEquals(RequestType.REGISTER, request.getRequestType());
        assertEquals("123456", request.getLogin());
        assertEquals("pwd", request.getPassword());
    }

    @Test
    public void parseGetBalanceRequest() throws Exception {
        String registerRequest = getFileText("get_balance_request");

        Request request = XmlParser.parseRequest(registerRequest);

        assertEquals(RequestType.GET_BALANCE, request.getRequestType());
        assertEquals("654321", request.getLogin());
        assertEquals("pwd2", request.getPassword());
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError() throws Exception {
        String registerRequest = getFileText("bad_request");
        XmlParser.parseRequest(registerRequest);
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError2() throws Exception {
        String registerRequest = getFileText("bad_request2");
        XmlParser.parseRequest(registerRequest);
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError3() throws Exception {
        String registerRequest = getFileText("bad_request3");
        XmlParser.parseRequest(registerRequest);
    }

    @Test
    public void serializeRegisterOkResponse() throws Exception {
        Response okResponse = Response.successRegister();

        String serialized = XmlParser.serialize(okResponse);
        String exprected = getFileText("register_response_ok");
        assertXmlEquals(exprected, serialized);
    }

    private void assertXmlEquals(String exprectedString, String actualString) throws Exception {
        assertEquals(prettyAsXml(exprectedString), prettyAsXml(actualString));
    }

    @Test
    public void serializeRegisterErrorResponse() throws Exception {
        Response errorResponse = Response.error(ResponseCode.UNKNOWN_ERROR);

        String serialized = XmlParser.serialize(errorResponse);
        String exprected = getFileText("register_response_error");
        assertXmlEquals(exprected, serialized);
    }

    @Test
    public void serializeGetBalanceOkResponse() throws Exception {
        Response okResponse = Response.successGetBalance(99d);

        String serialized = XmlParser.serialize(okResponse);
        String exprected = getFileText("get_balance_response_ok");
        assertXmlEquals(exprected, serialized);
    }

    @Test
    public void serializeGetBalanceErrorResponse() throws Exception {
        Response errorResponse = Response.error(ResponseCode.PASSWORD_INCORRECT);

        String serialized = XmlParser.serialize(errorResponse);
        String exprected = getFileText("get_balance_response_error");
        assertXmlEquals(exprected, serialized);
    }

    private String getFileText(String fileName) throws IOException {
        URL url = Resources.getResource("xmlparsertest/" + fileName + ".xml");
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
