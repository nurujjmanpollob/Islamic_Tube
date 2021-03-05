/*
 *
 *  Copyright 2021 Nurujjaman Pollob.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */


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
