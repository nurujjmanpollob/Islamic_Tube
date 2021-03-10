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

package com.ibrahimhossain.app;

import com.ibrahimhossain.app.BackgroundWorker.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthorProfileDetailsParser {


    private  String arrayToLook;
    private  String jsonInput;
    private AuthorProfileDetailsDatabaseListener listener;

    public  AuthorProfileDetailsParser(String rootArrayName, String input){

        this.arrayToLook = rootArrayName;
        this.jsonInput = input;

    }

    public void parseVideoDatabaseForResult(){

        AuthorProfileDetailsDatabase result = parseDatabase();
        if(result != null){

            if(listener != null){

                listener.onSuccessfulData(result);
            }
        }

    }

    private AuthorProfileDetailsDatabase parseDatabase(){


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
                    String name = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_NAME);
                    String address = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_HOME_ADDRESS);
                    String avatarURL = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_AVATAR_URL);
                    String email = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_EMAIL);
                    String facebookLink = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_FACEBOOK_ID_LINK);
                    String whatsappNumber = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_WHATSAPP_NO);
                    String linkedinID = childObject.getString(Variables.AUTHOR_PROFILE_AUTHOR_LINKEDIN_ID);



                    //return database
                    return new AuthorProfileDetailsDatabase(name,address,avatarURL,email,facebookLink,whatsappNumber,linkedinID);

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



    public interface AuthorProfileDetailsDatabaseListener{

        void onNullValueOrInput();
        void onArrayNotFound(String cause);
        void onSingleObjectNotFound(String cause);
        void onSuccessfulData(AuthorProfileDetailsDatabase videoDetailsDatabase);

    }


    public void setListener(AuthorProfileDetailsDatabaseListener eventListener){

        this.listener = eventListener;
    }



    
}
