package com.github.ibykhalov.simpleweb.exception;

public class XmlSerializationException extends ApplicationException {
    public XmlSerializationException() {
    }

    public XmlSerializationException(String message) {
        super(message);
    }

    public XmlSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlSerializationException(Throwable cause) {
        super(cause);
    }

    public XmlSerializationException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
