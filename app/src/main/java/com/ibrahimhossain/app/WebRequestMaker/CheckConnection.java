package com.ibrahimhossain.app.WebRequestMaker;

import com.ibrahimhossain.app.BackgroundWorker.CustomAsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;



public class CheckConnection extends CustomAsyncTask<Void, Boolean> {

    String hostName="8.8.8.8";
    int port = 53;
    int timeOut = 1500;

    ResultEvent event;

    @Override
    protected Boolean doBackgroundTask() {

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(hostName, port), timeOut);
            socket.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onTaskFinished(Boolean aBoolean) {

        if(event != null){

            this.event.isReallyOnline(aBoolean);
        }

        super.onTaskFinished(aBoolean);
    }

    public void setHostName(String hostName){

        this.hostName = hostName;

    }

    public void setPort(int port){

        this.port = port;
    }

    public void setTimeOut(int timeInMilliSecond){

        this.timeOut = timeInMilliSecond;
    }

    public interface ResultEvent{

        public Object isReallyOnline(Boolean flag);
    }

    public void listenOnLoadingEvent(ResultEvent eventListener){

        this.event = eventListener;
    }
}
