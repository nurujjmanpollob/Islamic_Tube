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

import com.ibrahimhossain.app.Variables;
import com.ibrahimhossain.app.VideoData;
import com.ibrahimhossain.app.VideoDetailsDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnusedDeclaration"})
public class JSONParser {

    String input;
    String arrayName;

    List<VideoData> result = new ArrayList<>();

    OnJSONParseEvent jsonParseEvent;

    public JSONParser(String input, String jsonArrayName){


        this.input = input;
        arrayName = jsonArrayName;

        
    }

    public void Parse(){

        List<VideoData> varifiedData = getParsedData();

        if(varifiedData != null && varifiedData.size() > 0){

            if(jsonParseEvent != null){
                jsonParseEvent.onSuccessfullyParse(varifiedData);
            }

        }else {
            if(jsonParseEvent != null){
                jsonParseEvent.onNullInput();
            }
        }
    }
    private List<VideoData> getParsedData(){

        //Check if input is null
        if(input != null){

            //Create Raw Object and get array
            try {

                JSONObject jsonObject = new JSONObject(input);

                //Get the array
                JSONArray jsonArray = jsonObject.getJSONArray(arrayName);

                //Get Array length
                int arraylength = jsonArray.length();

                //Lets loop inside array
                for (int i = 0; i < arraylength; i++){

                    //Get single object from JsonArray
                    JSONObject node = jsonArray.getJSONObject(i);

                    try {
                        //Get video title
                        String videoTitle = node.getString(Variables.VIDEO_JSON_TITLE);

                        //Get video description
                        String videoDescription = node.getString(Variables.VIDEO_JSON_DESCRIPTION);

                        //Get thumbnail url
                        String videoThumbnail = node.getString(Variables.VIDEO_JSON_THUMBNAIL);

                        //Get Video URL
                        String videoURL = node.getString(Variables.VIDEO_JSON_URL);

                        //add title description etc to database
                        result.add(new VideoData(videoTitle, videoDescription, videoURL, videoThumbnail));
                    }catch (Exception es){

                        //single object failed
                        if(jsonParseEvent != null){
                            jsonParseEvent.singleItemFailed(es.toString(), result);
                        }

                        return null;

                    }
                }

            } catch (JSONException e) {

                //Array contains wrong things
                if(jsonParseEvent != null){
                    jsonParseEvent.containsErrorInArray(e.toString(), result);
                }

                e.printStackTrace();

                return null;
            }


        }
        //Input is null
        else {

            if(jsonParseEvent != null){
                jsonParseEvent.onNullInput();
            }

            return null;

        }



        return result;
    }


    public interface OnJSONParseEvent{

        void onSuccessfullyParse(List<VideoData> videoDataList);
        void onNullInput();
        void containsErrorInArray(String cause, List<VideoData> dataParsedSoFar);
        void singleItemFailed(String cause, List<VideoData> dataParsedSoFar);

    }

    public void setListener(OnJSONParseEvent eventListener){

        this.jsonParseEvent = eventListener;
    }


    public static class VideoDetailsParser{
        private  String arrayToLook;
      private  String jsonInput;
      private VideoDetailsDatabaseListener listener;

       public void ModeVideoDetailsDatabase(String rootArrayName, String input){

           this.arrayToLook = rootArrayName;
           this.jsonInput = input;

       }

       public void parseVideoDatabaseForResult(){

           VideoDetailsDatabase result = parseDatabase();
           if(result != null){

               if(listener != null){

                   listener.onSuccessfulData(result);
               }
           }

       }

       private VideoDetailsDatabase parseDatabase(){


           //Check if input is null
           if(jsonInput != null){

               //Create Raw Object and get array
               try {

                   JSONObject jsonObject = new JSONObject(jsonInput);

                   //Get the array
                   JSONArray jsonArray = jsonObject.getJSONArray(arrayToLook);

                   //Get Array length
                   int arraylength = jsonArray.length();


                       //Get single object from JsonArray
                       JSONObject childObject = jsonArray.getJSONObject(0);

                       try {
                           String title = childObject.getString(Variables.VIDEO_DETAILS_TITLE);
                           String description = childObject.getString(Variables.VIDEO_DETAILS_DESCRIPTION);
                           String videoURL = childObject.getString(Variables.VIDEO_DETAILS_VIDEO_URL);
                           String authorName = childObject.getString(Variables.VIDEO_DETAILS_AUTHOR_NAME);
                           String authorAvatarURL = childObject.getString(Variables.VIDEO_DETAILS_AUTHOR_AVATAR);
                           String websiteReference = childObject.getString(Variables.VIDEO_DETAILS_WEBSITE_URL);
                           String videoThumbnail = childObject.getString(Variables.VIDEO_DETAILS_THUMBNAIL);
                           String authorProfileURL = childObject.getString(Variables.VIDEO_DETAILS_AUTHOR_PROFILE_URL);


                           //return database
                           return new VideoDetailsDatabase(title, description, authorName, authorAvatarURL, videoURL, videoThumbnail, websiteReference, authorProfileURL);
                       }catch (Exception es){

                           //single object failed
                           if(listener != null){
                               listener.onSingleObjectNotFound(es.toString());
                           }

                           return null;

                       }


               } catch (JSONException e) {

                   //Array contains wrong things
                   if(listener != null){
                       listener.onArrayNotFound(e.toString());
                   }

                   e.printStackTrace();

                   return null;
               }


           }
           //Input is null
           else {

               if(listener != null){
                   listener.onNullValueOrInput();
               }

               return null;

           }


       }



       public interface VideoDetailsDatabaseListener{

           void onNullValueOrInput();
           void onArrayNotFound(String cause);
           void onSingleObjectNotFound(String cause);
           void onSuccessfulData(VideoDetailsDatabase videoDetailsDatabase);

       }


       public void setListenerForVideoDetailsDatabase(VideoDetailsDatabaseListener eventListener){

           this.listener = eventListener;
       }


    }


}
