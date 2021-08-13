package com.example.bootiful.model;


public class ErrMessage extends ResponseMessage{


    private String status = "400";
    private String error = "Not Found";
    private String message = "no address parameter within the client request";
    private String path;

    public ErrMessage (String path){
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
