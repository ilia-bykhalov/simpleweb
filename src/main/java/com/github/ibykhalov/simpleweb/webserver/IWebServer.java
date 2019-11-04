package com.github.ibykhalov.simpleweb.webserver;

public interface IWebServer {
    void start();
    void stop();

    interface IRequestHandler {
        Response execRequest(String body);
    }
}
