package com.github.ibykhalov.simpleweb.data;

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
    public String toString() {
        return "{\"Request\":{" + "\"requestType\":\"" + requestType + "\"" + ", \"login\":\"" + login + "\"" +
               ", \"password\":\"" + password + "\"" + "}}";
    }
}
