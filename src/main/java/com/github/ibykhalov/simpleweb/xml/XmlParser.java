package com.github.ibykhalov.simpleweb.xml;

import com.github.ibykhalov.simpleweb.data.Request;
import com.github.ibykhalov.simpleweb.data.RequestType;
import com.github.ibykhalov.simpleweb.data.Response;
import com.github.ibykhalov.simpleweb.data.ResponseCode;
import com.github.ibykhalov.simpleweb.exception.XmlParsingException;
import com.github.ibykhalov.simpleweb.exception.XmlSerializationException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public final class XmlParser {
    private XmlParser() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static Request parseRequest(String requestBody) throws XmlParsingException {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new StringReader(requestBody));
            Element rootNode = document.getRootElement();

            RequestType requestType = extractRequestType(rootNode);

            List<Element> extraList = (List<Element>) rootNode.getChildren("extra");
            checkState(2 == extraList.size());
            String login = extractSingleExtraField(extraList, "login");
            String password = extractSingleExtraField(extraList, "password");

            return new Request(requestType, login, password);
        } catch (Exception ex) {
            throw new XmlParsingException("body=\n" + requestBody, ex);
        }
    }

    public static String serialize(Response response) throws XmlSerializationException {
        try {
            Document document = composeDocument(response);
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            StringWriter stringWriter = new StringWriter();

            xmlOutputter.output(document, stringWriter);
            return stringWriter.toString();
        } catch (Exception ex) {
            throw new XmlSerializationException("object=\n" + response, ex);
        }
    }

    private static Document composeDocument(Response response) {
        Document document = new Document();
        document.setRootElement(new Element("response"));

        Element responseCode = new Element("result-code");
        responseCode.setText(String.valueOf(response.getResponseCode().getCode()));
        document.getRootElement().addContent(responseCode);

        if (response.getResponseCode() == ResponseCode.OK && response.getBalance().isPresent()) {
            Element balance = new Element("extra");
            balance.setAttribute("name", "balance");
            balance.setText(formatBalance(response.getBalance().get()));
            document.getRootElement().addContent(balance);
        }
        return document;
    }

    private static String formatBalance(int balance) {
        double floatBalance = (double) balance / 100;
        return String.format("%.2f", floatBalance);
    }

    @SuppressWarnings("unchecked")
    private static RequestType extractRequestType(Element rootNode) throws XmlParsingException {
        List<Element> list = (List<Element>) rootNode.getChildren("request-type");
        checkState(1 == list.size());
        String rawRequestType = list.get(0).getText();
        return parseRequestType(rawRequestType);
    }

    private static String extractSingleExtraField(List<Element> extraList, String name) {
        return extraList.stream()
                .filter(elem -> elem.getAttribute("name").getValue().equals(name))
                .findFirst()
                .get()
                .getText();
    }

    private static RequestType parseRequestType(String rawRequestType) throws XmlParsingException {
        if ("CREATE-AGT".equals(rawRequestType)) {
            return RequestType.REGISTER;
        } else if ("GET-BALANCE".equals(rawRequestType)) {
            return RequestType.GET_BALANCE;
        } else {
            throw new XmlParsingException("Unknown request type=" + rawRequestType);
        }
    }
}
