package com.example.exception;

public class AccountNotFoundException extends Exception {

    private int code;

    public AccountNotFoundException(String s, int code) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
