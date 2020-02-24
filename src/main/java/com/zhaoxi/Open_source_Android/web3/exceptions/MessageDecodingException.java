package com.zhaoxi.Open_source_Android.web3.exceptions;

/**
 * Encoding exception.
 */
public class MessageDecodingException extends RuntimeException {
    public MessageDecodingException(String message) {
        super(message);
    }

    public MessageDecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
