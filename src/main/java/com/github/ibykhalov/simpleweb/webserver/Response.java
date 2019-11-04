package com.github.ibykhalov.simpleweb.webserver;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Response {
    private final int status;
    private final String body;

    public Response(int status, String body) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return status == response.status && Objects.equal(body, response.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status, body);
    }

    @Override
    public String toString() {
        return "{\"Response\":{" + "\"status\":\"" + status + "\"" + ", \"body\":\"" + body + "\"" + "}}";
    }
}
