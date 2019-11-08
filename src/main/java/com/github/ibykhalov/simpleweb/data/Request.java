package com.github.ibykhalov.simpleweb.data;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Request {
    private final RequestType requestType;
    private final String login;
    private final String password;

    public Request(RequestType requestType, String login, String password) {
        this.requestType = checkNotNull(requestType);
        this.login = checkNotNull(login);
        this.password = checkNotNull(password);
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        return requestType == request.requestType && Objects.equal(login, request.login) &&
               Objects.equal(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestType, login, password);
    }

    @Override
    public String toString() {
        return "{\"Request\":{" + "\"requestType\":\"" + requestType + "\"" + ", \"login\":\"" + login + "\"" +
               ", \"password\":\"" + password + "\"" + "}}";
    }
}

