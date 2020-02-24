package com.zhaoxi.Open_source_Android.web3.crypto;

/**
 * Cipher exception wrapper.
 * @author Aimee on 2018/5/29
 */
public class CipherException extends Exception {

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
