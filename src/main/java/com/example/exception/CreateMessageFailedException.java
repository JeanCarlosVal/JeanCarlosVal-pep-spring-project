package com.example.exception;

public class CreateMessageFailedException extends Exception {

    private int code;

    public CreateMessageFailedException(String s, int code) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
