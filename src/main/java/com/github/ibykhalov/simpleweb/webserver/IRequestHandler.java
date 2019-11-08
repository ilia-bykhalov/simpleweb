package com.github.ibykhalov.simpleweb.webserver;

public interface IRequestHandler {
    HttpResponse execRequest(String body);
}
