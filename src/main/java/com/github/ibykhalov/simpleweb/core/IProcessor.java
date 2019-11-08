package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.xml.data.Request;
import com.github.ibykhalov.simpleweb.xml.data.Response;

public interface IProcessor {
    Response processRequest(Request request);
}
