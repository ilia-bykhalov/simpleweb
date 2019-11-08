package com.github.ibykhalov.simpleweb.exception;

public class XmlParsingException extends ApplicationException {
    public XmlParsingException() {
    }

    public XmlParsingException(String message) {
        super(message);
    }

    public XmlParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlParsingException(Throwable cause) {
        super(cause);
    }

    public XmlParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
