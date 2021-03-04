package com.ibrahimhossain.app.BackgroundWorker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestMaker extends CustomAsyncTask<Void, String> {



    String url;
    int timeOut = 1500;
    WebRequestEvent event;

    public WebRequestMaker(String url){

        this.url = url;

    }


    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    protected void preExecute() {


            event.onStartExecuting();


        super.preExecute();
    }

    @Override
    protected String doBackgroundTask() {

            event.onLoadingTask();

        StringBuilder result = new StringBuilder();
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
                while((line = bfr.readLine()) != null){

                    result.append(line);
                    result.append("\n");
                }
            }

        }
        catch (Exception e)
        {


            event.onLoadFailed(e.toString());

            return null;
        }

        return result.toString();
    }


    @Override
    protected void onTaskFinished(String s) {



            if (s != null ){
                event.onTaskFinished(s);


        }


        super.onTaskFinished(s);
    }


    public interface WebRequestEvent{

        void onStartExecuting();
        void onLoadingTask();
         void onTaskFinished(String result);
         void onLoadFailed(String cause);
    }



    public void setEventListener(WebRequestEvent eventListener){

        this.event = eventListener;
    }



}
