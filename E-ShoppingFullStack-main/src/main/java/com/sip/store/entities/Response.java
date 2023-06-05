package com.sip.store.entities;

public class Response {
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response() {
        super();
    }

    public Response(String message) {
        super();
        this.message = message;
    }
}
