package com.github.ibykhalov.simpleweb.xml;

import com.github.ibykhalov.simpleweb.exception.XmlParsingException;
import com.github.ibykhalov.simpleweb.xml.data.Request;
import com.github.ibykhalov.simpleweb.xml.data.RequestType;
import com.github.ibykhalov.simpleweb.xml.data.Response;
import com.github.ibykhalov.simpleweb.xml.data.ResponseCode;
import org.junit.Test;

import java.io.IOException;

import static com.github.ibykhalov.simpleweb.TestUtils.assertXmlEquals;
import static com.github.ibykhalov.simpleweb.TestUtils.getFileText;
import static org.junit.Assert.assertEquals;

public class XmlParserTest {
    @Test
    public void parseRegisterRequest() throws Exception {
        String registerRequest = getTestString("register_request");

        Request request = XmlParser.parseRequest(registerRequest);

        assertEquals(RequestType.REGISTER, request.getRequestType());
        assertEquals("123456", request.getLogin());
        assertEquals("pwd", request.getPassword());
    }

    @Test
    public void parseGetBalanceRequest() throws Exception {
        String registerRequest = getTestString("get_balance_request");

        Request request = XmlParser.parseRequest(registerRequest);

        assertEquals(RequestType.GET_BALANCE, request.getRequestType());
        assertEquals("654321", request.getLogin());
        assertEquals("pwd2", request.getPassword());
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError() throws Exception {
        String registerRequest = getTestString("bad_request");
        XmlParser.parseRequest(registerRequest);
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError2() throws Exception {
        String registerRequest = getTestString("bad_request2");
        XmlParser.parseRequest(registerRequest);
    }

    @Test(expected = XmlParsingException.class)
    public void xmlExceptionOnParseError3() throws Exception {
        String registerRequest = getTestString("bad_request3");
        XmlParser.parseRequest(registerRequest);
    }

    @Test
    public void serializeRegisterOkResponse() throws Exception {
        Response okResponse = Response.successRegister();

        String serialized = XmlParser.serialize(okResponse);
        String exprected = getTestString("register_response_ok");
        assertXmlEquals(exprected, serialized);
    }

    @Test
    public void serializeRegisterErrorResponse() throws Exception {
        Response errorResponse = Response.error(ResponseCode.UNKNOWN_ERROR);

        String serialized = XmlParser.serialize(errorResponse);
        String exprected = getTestString("register_response_error");
        assertXmlEquals(exprected, serialized);
    }

    @Test
    public void serializeGetBalanceOkResponse() throws Exception {
        Response okResponse = Response.successGetBalance(99d);

        String serialized = XmlParser.serialize(okResponse);
        String exprected = getTestString("get_balance_response_ok");
        assertXmlEquals(exprected, serialized);
    }

    @Test
    public void serializeGetBalanceErrorResponse() throws Exception {
        Response errorResponse = Response.error(ResponseCode.PASSWORD_INCORRECT);

        String serialized = XmlParser.serialize(errorResponse);
        String exprected = getTestString("get_balance_response_error");
        assertXmlEquals(exprected, serialized);
    }

    private String getTestString(String fileName) throws IOException {
        return getFileText("xmlparsertest/" + fileName + ".xml");
    }
}
