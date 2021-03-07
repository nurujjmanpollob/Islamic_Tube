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

import android.webkit.URLUtil;

@SuppressWarnings({"UnusedDeclaration"})
public class IsValidYoutubeURL {

    String input;
    ListenerOnURLEvent urlEvent;

    @SuppressWarnings({"UnusedDeclaration"})
   public IsValidYoutubeURL(String url){

       this.input = url;

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void runTest(){

       //first we need to check that url is valid or not
        if(!URLUtil.isValidUrl(input)){

            if(urlEvent != null){

                urlEvent.invalidURL();
            }
        }
        //URL is valid
        else {

            String ytV1 = "https://www.youtube.com/watch?v=";
            String ytV2 = "https://youtu.be/";

            String result = getVideoIDOrDirectURL(ytV1, ytV2, input);

            if(result.startsWith("http://") || result.startsWith("https://")){

                if(urlEvent != null){

                    urlEvent.regularVideo(result);

                }
            }else {

                if(urlEvent != null){

                    urlEvent.youtubeVideoKey(result);

                }
            }



        }

    }

    @SuppressWarnings({"UnusedDeclaration"})
    private String getVideoIDOrDirectURL(String ytPattern1, String ytPattern2, String url){

       int lengthOfInput = url.length();

       if(url.substring(0, ytPattern1.length()).equals(ytPattern1)){

           return url.substring(ytPattern1.length(), lengthOfInput);
       }else {

           if(url.substring(0, ytPattern2.length()).equals(ytPattern2)){
               return url.substring(ytPattern2.length(), lengthOfInput);
           }else {
               return url;
           }
       }

    }


    public interface ListenerOnURLEvent{

       void invalidURL();
       void regularVideo(String videoURL);
       void youtubeVideoKey(String key);

    }


    public void setListenerForURLEvent(ListenerOnURLEvent listenerForURLEvent){

        this.urlEvent = listenerForURLEvent;
    }


}
