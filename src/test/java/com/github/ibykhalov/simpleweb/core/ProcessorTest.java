package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.db.Database;
import com.github.ibykhalov.simpleweb.xml.data.Request;
import com.github.ibykhalov.simpleweb.xml.data.RequestType;
import com.github.ibykhalov.simpleweb.xml.data.Response;
import com.github.ibykhalov.simpleweb.xml.data.ResponseCode;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;

import java.sql.*;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {

    @Before
    public void setUp() throws Exception {
        new Database().truncate();
    }

    @Test
    public void register() {
        Processor processor = new Processor(new Database());

        Request request = new Request(RequestType.REGISTER, "login", "pass");
        Response response = processor.processRequest(request);

        assertEquals(Response.successRegister(), response);
    }

    @Test
    public void doubleRegister() {
        register();
        Processor processor = new Processor(new Database());

        Request request = new Request(RequestType.REGISTER, "login", "pass");
        Response response = processor.processRequest(request);

        assertEquals(Response.error(ResponseCode.USER_ALREADY_EXISTS), response);
    }

}
