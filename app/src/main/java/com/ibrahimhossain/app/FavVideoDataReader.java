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

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibrahimhossain.app.BackgroundWorker.CustomAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FavVideoDataReader extends CustomAsyncTask<Void, List<FavoriteVideoDatabase>> {


    private Context context;
    private OnFavVideoDataLoadEvent event;
    List<FavoriteVideoDatabase> databases;


    public FavVideoDataReader(Context context){

        this.context = context;


    }


    @Override
    protected List<FavoriteVideoDatabase> doBackgroundTask() {


        SharedPreferences sharedPreferences = context.getSharedPreferences(Variables.FAVORITE_VIDEO_DATASHEET_NAME, Context.MODE_PRIVATE);
        String jsonLink = sharedPreferences.getString(Variables.FAVORITE_VIDEO_URL, null);
        String jsonTitle = sharedPreferences.getString(Variables.FAVORITE_VIDEO_TITLE, null);

          databases  = new ArrayList<>();

        if(jsonLink != null && jsonTitle != null){

            Gson gson = new Gson();
            List<String> linkArray = gson.fromJson(jsonLink, new TypeToken<List<String>>() {
            }.getType());

            List<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<List<String>>() {
            }.getType());

            //Run the loop and add all database
            for(int i = 0; i < linkArray.size(); i++){


                databases.add(new FavoriteVideoDatabase(titleArray.get(i), linkArray.get(i)));



            }

            //return database
            return databases;

        }else {


            //Database is simply empty
            return null;


        }

    }

    @Override
    protected void onTaskFinished(List<FavoriteVideoDatabase> favoriteVideoDatabases) {

        if(favoriteVideoDatabases != null){

            if(event != null){
                event.onHasDatabase(favoriteVideoDatabases);
            }


        }else {

            if(event != null){

                event.onNullDatabase();
            }


        }

        super.onTaskFinished(favoriteVideoDatabases);
    }


    public interface OnFavVideoDataLoadEvent{

        void onHasDatabase(List<FavoriteVideoDatabase> databaseList);
        void onNullDatabase();
    }

    public void setEvent(OnFavVideoDataLoadEvent event){

        this.event = event;
    }
}
