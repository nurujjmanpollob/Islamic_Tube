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


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class HTMLThemeColorGetter
{

    static String data = "";

    static String urlOfHTML = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public HTMLThemeColorGetter(final String remoteHTMLURL){

        urlOfHTML = remoteHTMLURL;

        SyncTask sy = new SyncTask();
        sy.defineThreadCount(4);
        sy.runThread();



    }






    private String extractColorFromMeta(String html){

        StringBuilder tmp = new StringBuilder();

        for(int i = 0; i < html.length(); i++){

            String s = Character.toString(html.charAt(i));

            if ("<".equals(s)) {
                try {
                    String m = Character.toString(html.charAt(i + 1));
                    String e = Character.toString(html.charAt(i + 2));
                    String t = Character.toString(html.charAt(i + 3));
                    String a = Character.toString(html.charAt(i + 4));
                    String Spc = Character.toString(html.charAt(i + 5));
                    String n = Character.toString(html.charAt(i + 6));
                    String na = Character.toString(html.charAt(i + 7));
                    String nam = Character.toString(html.charAt(i + 8));
                    String name = Character.toString(html.charAt(i + 9));
                    String equl = Character.toString(html.charAt(i + 10));
                    String strtQ = Character.toString(html.charAt(i + 11));
                    String th = Character.toString(html.charAt(i + 12));
                    String th1 = Character.toString(html.charAt(i + 13));
                    String the = Character.toString(html.charAt(i + 14));
                    String them = Character.toString(html.charAt(i + 15));
                    String theme = Character.toString(html.charAt(i + 16));
                    String dash = Character.toString(html.charAt(i + 17));
                    String c = Character.toString(html.charAt(i + 18));
                    String o = Character.toString(html.charAt(i + 19));
                    String l = Character.toString(html.charAt(i + 20));
                    String colo = Character.toString(html.charAt(i + 21));
                    String r = Character.toString(html.charAt(i + 22));
                    String endQ = Character.toString(html.charAt(i + 23));
                    int startingIndex = i + 24;

                    if (m.equals("m") &&
                            e.equals("e") &&
                            t.equals("t") &&
                            a.equals("a") &&
                            Spc.equals(" ") &&
                            n.equals("n") &&
                            na.equals("a") &&
                            nam.equals("m") &&
                            name.equals("e") &&
                            equl.equals("=") &&
                            strtQ.equals("\"") &&
                            th.equals("t") &&
                            th1.equals("h") &&
                            the.equals("e") &&
                            them.equals("m") &&
                            theme.equals("e") &&
                            dash.equals("-") &&
                            c.equals("c") &&
                            o.equals("o") &&
                            l.equals("l") &&
                            colo.equals("o") &&
                            r.equals("r") &&
                            endQ.equals("\"")) {

                        //start reading rest data!

                        for (int fc = startingIndex; fc < html.length(); fc++) {

                            String ss = Character.toString(html.charAt(fc));

                            if (">".equals(ss)) {
                                return getValueFromContent(tmp.toString());
                            } else {
                                tmp.append(html.charAt(fc));
                            }
                        }
                    }

                } catch (Exception ess) {
                    if (onGetEvent != null)
                        onGetEvent.onExceptionOccured(ess.toString());
                }
            }
        }
        return null;
    }


    private String getValueFromContent(String input){

        StringBuilder result = new StringBuilder();

        for(int in = 0; in < input.length(); in++){

            String s = Character.toString(input.charAt(in));

            if ("c".equals(s)) {
                try {
                    String o = Character.toString(input.charAt(in + 1));
                    String n = Character.toString(input.charAt(in + 2));
                    String t = Character.toString(input.charAt(in + 3));
                    String e = Character.toString(input.charAt(in + 4));
                    String en = Character.toString(input.charAt(in + 5));
                    String nt = Character.toString(input.charAt(in + 6));
                    String eqel = Character.toString(input.charAt(in + 7));
                    String startQ = Character.toString(input.charAt(in + 8));
                    int nextIndex = in + 9;

                    if (o.equals("o") &&
                            n.equals("n") &&
                            t.equals("t") &&
                            e.equals("e") &&
                            en.equals("n") &&
                            nt.equals("t") &&
                            eqel.equals("=") &&
                            startQ.equals("\"")) {

                        //start for loop

                        for (int plb = nextIndex; plb < input.length(); plb++) {

                            String fs = Character.toString(input.charAt(plb));

                            if ("\"".equals(fs)) {
                                return result.toString();
                            } else {
                                result.append(input.charAt(plb));
                            }
                        }
                    }


                } catch (Exception ess) {
                    if (onGetEvent != null)
                        onGetEvent.onExceptionOccured(ess.toString());
                }
            }
        }

        return null;
    }


    public interface ListenOnGetEvent{

        void onSuccessfullColorExtraction(String colorStr);
        void onExceptionOccured(String exceptionMsg);

    }

    public ListenOnGetEvent onGetEvent = null;

    public void ListenOnLoadEvent(ListenOnGetEvent eventListerner){

        onGetEvent = eventListerner;

    }



    private class SyncTask extends CustomAsyncTask<Void, String>
    {



        @Override
        protected String doBackgroundTask()
        {


            URL url;
            StringBuilder response = new StringBuilder();

            try{

                url = new URL(urlOfHTML);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK){

                    String line;
                    BufferedReader bfReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while((line = bfReader.readLine()) != null){

                        response.append(line);
                    }
                }
                else{

                    response = new StringBuilder();

                }


            }catch(Exception ess){


                data = ess.toString();



                if(onGetEvent != null)
                    onGetEvent.onExceptionOccured(ess.toString());

                return null;

            }


            data = response.toString();

            return data;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected void onTaskFinished(String result)
        {
            if(onGetEvent != null && result != null){

                onGetEvent.onSuccessfullColorExtraction(extractColorFromMeta(data));

            }
            super.onTaskFinished(result);
        }




    }
}

