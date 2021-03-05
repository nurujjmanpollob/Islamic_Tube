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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrahimhossain.app.BackgroundWorker.ThreadFixer;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.WebRequestMaker.VideoAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivty extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    CircleImageView profileImageView;
    AppCompatTextView titleView;

    private KProgressHUD progressHUD;

    AppCompatTextView errorTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set full screen mode
        if(Objects.requireNonNull(getSupportActionBar()).isShowing()){

            getSupportActionBar().hide();
        }



        setContentView(R.layout.activity_main);


        //Initialize views
        toolbar = findViewById(R.id.main_toolbar);
        profileImageView = findViewById(R.id.main_toolbar_profile_image);
        titleView = findViewById(R.id.main_toolbar_text_view);
        recyclerView = findViewById(R.id.main_recycler_view);
        errorTxt = findViewById(R.id.main_error_text_view);



        // This helper class will help to load json easily and provides various callbacks

        WebRequestMaker webRequestMaker = new WebRequestMaker(Variables.HOME_JSON_URL);
        webRequestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {
            @Override
            public void onStartExecuting() {

                progressHUD = new KProgressHUD(MainActivty.this);
                progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                progressHUD.setLabel("Loading");
                progressHUD.show();

            }

            @Override
            public void onLoadingTask() {

            }

            @Override
            public void onTaskFinished(String result) {



                //Our call is now successful, lets parse all json data

                //We are using custom threads(by default a newly created thread) that doesn't
                // Run onUIThread, and If you going to update current views that not created by Thread we have used
                //To do the separated task, we will get CalledFromWrongThreadException
                //Separated thread help us greatly improve performance in multi core processor
                //Without slowing the UI
                // So using ThreadFixer with current main Thread instance would simply fix this exception


                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        //Set Layout manager
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivty.this));

                        //Call new instance of video adapter
                        VideoAdapter adapter = new VideoAdapter(MainActivty.this, getAllJsonData(result));

                        //set recyclerview adapter
                        recyclerView.setAdapter(adapter);

                    }
                });


                    progressHUD.dismiss();



            }


            @Override
            public void onLoadFailed(String cause) {

                        progressHUD.dismiss();

                        errorTxt.setVisibility(View.VISIBLE);



            }
        });

        webRequestMaker.runThread();

    }


    private List<VideoData> getAllJsonData(String input){

        //Add database instance
        List<VideoData> videoData = new ArrayList<>();


        if(input != null){



            //Start try catch statement for whole array

            try{

                //Create a new instance of object and later on we will fetch elements by Array
                JSONObject jsonObject = new JSONObject(input);

                //Get the object root
                JSONArray jsonArray = jsonObject.getJSONArray(Variables.VIDEO_JSON_ROOT);

                //get length of database
                int dataLength = jsonArray.length();

                //Lets run loop and get single data from database
                for(int i = 0; i < dataLength; i++){

                    //get single node from array
                    JSONObject node = jsonArray.getJSONObject(i);

                    //Again try / catch block to ensure the application will never crash, and this is all for debugging purpose
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
                        videoData.add(new VideoData(videoTitle, videoDescription, videoURL, videoThumbnail));

                    }catch (Exception exception){

                        Log.d(Variables.VIDEO_LOG_TAG, exception.toString());

                        errorTxt.setVisibility(View.VISIBLE);
                                progressHUD.dismiss();


                        return videoData;

                    }
                }




            }catch (Exception exception){

                Log.d(Variables.VIDEO_LOG_TAG, exception.toString());

                Toast.makeText(MainActivty.this, "It is appears that no database installed", Toast.LENGTH_LONG).show();

                progressHUD.dismiss();

                errorTxt.setVisibility(View.VISIBLE);



            }

        }else {

            Log.d(Variables.VIDEO_LOG_TAG, "The coding glitch happened! Sorry for this inconvenience");



            errorTxt.setVisibility(View.VISIBLE);

                    progressHUD.dismiss();


            videoData.add(new VideoData("Top level Error!", "Please contact the developer.", null, null));

        }
        return videoData;
    }

    public KProgressHUD getProgressHUD() {
        return progressHUD;
    }
}
