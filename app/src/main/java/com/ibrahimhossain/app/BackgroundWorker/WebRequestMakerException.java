package com.ibrahimhossain.app.BackgroundWorker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WebRequestMakerException extends Exception {


    String message;

    public WebRequestMakerException(String errorMessage){

        super(errorMessage);
        this.message = errorMessage;

    }

    @NonNull
    @Override
    public String toString() {
       return message;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
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

    @NonNull
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}
