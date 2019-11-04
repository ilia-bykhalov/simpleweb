package com.github.ibykhalov.simpleweb.xml.data;

import com.google.common.base.Objects;

import java.util.Optional;

import static com.github.ibykhalov.simpleweb.xml.data.ResponseCode.OK;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

public class Response {
    private final ResponseCode responseCode;
    private final Double balance;

    private Response(ResponseCode responseCode, Double balance) {
        this.responseCode = checkNotNull(responseCode);
        this.balance = balance;
    }

    public static Response successRegister() {
        return new Response(OK, null);
    }

    public static Response successGetBalance(double balance) {
        return new Response(OK, balance);
    }

    public static Response error(ResponseCode errorCode) {
        return new Response(errorCode, null);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public Optional<Double> getBalance() {
        return ofNullable(balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response that = (Response) o;
        return Double.compare(that.balance, balance) == 0 && responseCode == that.responseCode;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(responseCode, balance);
    }

    @Override
    public String toString() {
        return "{\"Response\":{" + "\"responseCode\":\"" + responseCode + "\"" + ", \"balance\":\"" + balance + "\"" +
               "}}";
    }
}
