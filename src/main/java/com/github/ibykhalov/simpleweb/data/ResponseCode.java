package com.github.ibykhalov.simpleweb.data;

public enum ResponseCode {
    OK(0, true, true),
    USER_ALREADY_EXISTS(1, true, false),
    UNKNOWN_ERROR(2, true, true),
    USER_NOT_FOUND(3, false, true),
    PASSWORD_INCORRECT(4, false, true);

    private final int code;
    private final boolean registerAllowed;
    private final boolean getBalanceAllowed;

    ResponseCode(int code, boolean registerAllowed, boolean getBalanceAllowed) {
        this.code = code;
        this.registerAllowed = registerAllowed;
        this.getBalanceAllowed = getBalanceAllowed;
    }

    public int getCode() {
        return code;
    }

    public boolean isRegisterAllowed() {
        return registerAllowed;
    }

    public boolean isGetBalanceAllowed() {
        return getBalanceAllowed;
    }
}
