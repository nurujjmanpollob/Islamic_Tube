package com.ibrahimhossain.app.WebRequestMaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class InternetResultClassNotImplementedException extends Exception {

    String msg = "No message has been added";

    public InternetResultClassNotImplementedException(String message){


        this.msg = message;
    }

  @NonNull
    @Override
    public String toString() {
        return "InternetResultClassNotImplementedException{" +
                "msg='" + msg + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Nullable
    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Nullable
    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
