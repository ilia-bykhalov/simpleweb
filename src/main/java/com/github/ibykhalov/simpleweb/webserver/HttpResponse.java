package com.github.ibykhalov.simpleweb.webserver;

import static com.google.common.base.Preconditions.checkNotNull;

public final class HttpResponse {
    private final int status;
    private final String body;

    public HttpResponse(int status, String body) {
        this.status = status;
        this.body = checkNotNull(body);
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "{\"HttpResponse\":{" + "\"status\":\"" + status + "\"" + ", \"body\":\"" + body + "\"" + "}}";
    }
}
