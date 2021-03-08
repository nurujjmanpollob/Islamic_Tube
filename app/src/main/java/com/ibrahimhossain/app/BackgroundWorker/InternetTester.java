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


@SuppressWarnings({"UnusedDeclaration"})
public class InternetTester extends CustomAsyncTask<Void, String>{

    OnPingEvent pingEvent;
    Boolean isFailed = false;

    @SuppressWarnings({"UnusedDeclaration"})
    public static String PING_GOOGLE_SERVER = "https://www.google.com";

    String url;

    @SuppressWarnings({"UnusedDeclaration"})
    public InternetTester(@NonNull String url) {

        this.url = url;

    }

    @Override
    protected void preExecute() {

        if(pingEvent != null){

            pingEvent.onPingRunning();
        }
        super.preExecute();
    }

    @Override
    protected String doBackgroundTask() {


        StringBuilder result = new StringBuilder();
        try
        {
            URL urls = new URL(url);

            HttpURLConnection connct = (HttpURLConnection) urls.openConnection();

            connct.setDoInput(true);

            connct.setRequestMethod("GET");
            connct.setReadTimeout(1500);

            int response = connct.getResponseCode();

            if(response == HttpURLConnection.HTTP_OK){

                InputStream is = connct.getInputStream();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = bfr.readLine()) != null){

                    result.append(line);
                    result.append("\n");
                }
            }

        }
        catch (Exception e)
        {


            if(pingEvent != null) {
               pingEvent.onPingFailed();

            }

            isFailed = true;
            return null;
        }

        return result.toString();

    }

    @Override
    protected void onTaskFinished(String s) {

        if(s != null){
            if(pingEvent != null){

                pingEvent.onPingSuccess();
            }
        }else {

            if (!isFailed){

                if (pingEvent != null){
                    pingEvent.onPingFailed();
                }
            }
        }
        super.onTaskFinished(s);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public interface OnPingEvent{

        void onPingRunning();
        void onPingSuccess();
        void onPingFailed();


    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setPingListener(OnPingEvent pingEvent){

        this.pingEvent = pingEvent;

    }


}
