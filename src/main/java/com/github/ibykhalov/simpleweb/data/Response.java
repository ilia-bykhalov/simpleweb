package com.github.ibykhalov.simpleweb.data;

import java.util.Optional;

import static com.github.ibykhalov.simpleweb.data.ResponseCode.OK;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

public final class Response {
    private final ResponseCode responseCode;
    private final Integer balance;

    private Response(ResponseCode responseCode, Integer balance) {
        this.responseCode = checkNotNull(responseCode);
        this.balance = balance;
    }

    public static Response successRegister() {
        return new Response(OK, null);
    }

    public static Response successGetBalance(int balance) {
        return new Response(OK, balance);
    }

    public static Response error(ResponseCode errorCode) {
        return new Response(errorCode, null);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public Optional<Integer> getBalance() {
        return ofNullable(balance);
    }

    @Override
    public String toString() {
        return "{\"Response\":{" + "\"responseCode\":\"" + responseCode + "\"" + ", \"balance\":\"" + balance + "\"" +
               "}}";
    }
}
