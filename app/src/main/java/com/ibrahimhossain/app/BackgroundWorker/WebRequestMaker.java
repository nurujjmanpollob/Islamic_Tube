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


package com.ibrahimhossain.app.BackgroundWorker;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestMaker extends CustomAsyncTask<Void, String> {



    String url;
    int timeOut = 1500;
    WebRequestEvent event;


    StringBuilder result;

    Boolean isExceptionThrow;


    public WebRequestMaker(String url){

        this.url = url;

    }


    @SuppressWarnings({"UnusedDeclaration"})
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    protected void preExecute() {

        if(event != null) {
            event.onStartExecuting();
        }

        super.preExecute();
    }

    @Override
    protected String doBackgroundTask() {


        result = new StringBuilder();


        try
        {
            URL urls = new URL(url);

            HttpURLConnection connct = (HttpURLConnection) urls.openConnection();

            connct.setDoInput(true);
            connct.setRequestMethod("GET");
            connct.setReadTimeout(timeOut);

            int response = connct.getResponseCode();

            if(response == HttpURLConnection.HTTP_OK){

                InputStream is = connct.getInputStream();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
                String line;

                while(( line = bfr.readLine()) != null){

                    result.append(line);
                    result.append("\n");
                }
            }

        }
        catch (Exception e) {

            if(event != null) {
                event.onLoadFailed(e.toString());
            }
            return null;
        }

        return result.toString();
    }


    @Override
    protected void onTaskFinished(String s) {


        if(event != null && s != null) {
            event.onTaskFinished(s);
        }

        super.onTaskFinished(s);
    }


    public interface WebRequestEvent{

        void onStartExecuting();
         void onTaskFinished(String result);
         void onLoadFailed(String cause);
    }



    public void setEventListener(WebRequestEvent eventListener){

        this.event = eventListener;

    }




}
