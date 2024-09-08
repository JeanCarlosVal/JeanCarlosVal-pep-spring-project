package com.example.exception;

public class UpdateMessageException extends Exception {

    private int code;

    public UpdateMessageException(String s, int code) {
        super(s);

        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
