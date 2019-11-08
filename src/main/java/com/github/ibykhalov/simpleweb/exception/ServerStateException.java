package com.github.ibykhalov.simpleweb.exception;

public class ServerStateException extends RuntimeException {
    public ServerStateException() {
    }

    public ServerStateException(String message) {
        super(message);
    }

    public ServerStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerStateException(Throwable cause) {
        super(cause);
    }

    public ServerStateException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
