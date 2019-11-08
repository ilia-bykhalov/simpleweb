package com.github.ibykhalov.simpleweb.db;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public final class UserBalance {
    private final Integer value;
    private final GetBalanceError error;

    private UserBalance(Integer value, GetBalanceError error) {
        this.value = value;
        this.error = error;
    }

    public static UserBalance value(int value) {
        return new UserBalance(value, null);
    }

    public static UserBalance error(GetBalanceError error) {
        return new UserBalance(null, error);
    }

    public boolean hasValue() {
        return value != null;
    }

    public int getValue() {
        return value;
    }

    public GetBalanceError getError() {
        return checkNotNull(error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserBalance that = (UserBalance) o;
        return Objects.equal(value, that.value) && error == that.error;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, error);
    }

    @Override
    public String toString() {
        return "{\"UserBalance\":{" + "\"value\":\"" + value + "\"" + ", \"error\":\"" + error + "\"" + "}}";
    }
}
