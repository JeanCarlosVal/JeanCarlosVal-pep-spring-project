package com.example.exception;

public class RegisterAccountFailedException extends Exception {

    private int code;

    public RegisterAccountFailedException(String s, int code) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
