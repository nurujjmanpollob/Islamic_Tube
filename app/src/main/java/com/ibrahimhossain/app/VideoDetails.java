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

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.android.material.textview.MaterialTextView;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.dialogview.CacheUriPerser;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.ibrahimhossain.app.dialogview.NJPollobDialogWorker;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class VideoDetails extends AppCompatActivity {


    // create field for Video thumbnail view
    GifImageView thumbnailView;

    //Create field for Thumbnail view Root layout
    CircularRevealRelativeLayout thumbnailViewRootRelative;

    //Create field for Author title
    MaterialTextView authorNameView;

    //Create field for author avatar view
    CircleImageView authorAvatarView;

    //Video Share button view
    ImageFilterView shareButton;

    //Video Website reference view
    ImageFilterView websiteView;

    //Create field for video title view
    MaterialTextView videoTitleView;

    //Create field for video description view
    MaterialTextView videoDescriptionView;

    //Create field for Database;
    VideoDetailsDatabase database;

    //Create field for database url
    String databaseURL;

    //Create progress drawable;
    KProgressHUD kProgressHUD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //inflate layout from xml
        setContentView(R.layout.video_details_view);

        //Initialize views
        thumbnailView = findViewById(R.id.video_details_main_thumbnailview);
        thumbnailViewRootRelative = findViewById(R.id.video_details_main_images_root_relative);
        authorNameView = findViewById(R.id.video_details_profile_name);
        authorAvatarView = findViewById(R.id.video_details_profile_image);
        shareButton = findViewById(R.id.video_details_share);
        websiteView = findViewById(R.id.video_details_browse);
        videoTitleView = findViewById(R.id.video_details_main_titleview);
        videoDescriptionView = findViewById(R.id.video_details_main_descriptionview);

        //Check for intent data that passed from previous Activity
        if(getIntent().hasExtra(Variables.VIDEO_DETAILS_INTENT_KEY)){

            //assign database url
            databaseURL = getIntent().getStringExtra(Variables.VIDEO_DETAILS_INTENT_KEY);



        }

        //Retrieve current database by making web call
        WebRequestMaker requestMaker = new WebRequestMaker(databaseURL);
        requestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {
            @Override
            public void onStartExecuting() {

                kProgressHUD = new KProgressHUD(VideoDetails.this);
                kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                kProgressHUD.setLabel("Fetching database...");
                kProgressHUD.setCancellable(false);
                kProgressHUD.show();

            }

            @Override
            public void onLoadingTask() {

            }

            @Override
            public void onTaskFinished(String result) {

                //get database
                database = parseSingleVideoDatabase(result);

                //Check is database is null or not!
                if(!database.getIsNotNull()){

                    if (kProgressHUD.isShowing()) {
                        kProgressHUD.dismiss();
                    }

                    //Create alertDialog saying that actual error happened
                    NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                    dialog.setDialogDescription("The database is not found, usually because of no internet connection.");
                    dialog.setThumbnailByResource(R.drawable.error_500);
                    dialog.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                        @Override
                        public void onLeftButtonClick(View view) {

                        }

                        @Override
                        public void onRightButtonClick(View view) {

                            dialog.dismiss();

                        }
                    });


                    dialog.show();

                }else {



                    videoTitleView.setText(database.getVideoTitle());
                    videoDescriptionView.setText(database.getVideoDescription());
                    authorNameView.setText(database.getAuthorName());

                    //Set thumbnail image from server

                    NJPollobDialogWorker worker = new NJPollobDialogWorker(VideoDetails.this, false, database.getVideoThumbnail());
                    worker.setEventListenerForTask(new NJPollobDialogWorker.ListenOnResourceLoadEvent() {
                        @Override
                        public void onLoadingResource() {



                        }

                        @Override
                        public void onSuccessfullyExecution(String cacheDir, String fileName) {

                            //Let's parse downloaded resources and set downloaded image
                            CacheUriPerser perser = new CacheUriPerser(thumbnailView, cacheDir, fileName);
                            perser.setUpResources();

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }
                        }

                        @Override
                        public void onReceivedError(Exception exceptionToRead) {

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }

                        }
                    });


                    //Set author Image from server

                    NJPollobDialogWorker backgroundWorker = new NJPollobDialogWorker(VideoDetails.this, false, database.getAuthorAvatarURL());
                    backgroundWorker.setEventListenerForTask(new NJPollobDialogWorker.ListenOnResourceLoadEvent() {
                        @Override
                        public void onLoadingResource() {

                        }

                        @Override
                        public void onSuccessfullyExecution(String cacheDir, String fileName) {

                            //Let's parse downloaded resources and set downloaded image
                            CacheUriPerser perser = new CacheUriPerser(authorAvatarView, cacheDir, fileName);
                            perser.setUpResources();

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }

                        }

                        @Override
                        public void onReceivedError(Exception exceptionToRead) {

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }

                        }
                    });

                    
                }

            }

            @Override
            public void onLoadFailed(String cause) {

                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }


                //Create alertDialog saying that actual error happened
                NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                dialog.setDialogDescription(cause);
                dialog.setThumbnailByResource(R.drawable.error_500);
                dialog.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClick(View view) {

                    }

                    @Override
                    public void onRightButtonClick(View view) {

                        dialog.dismiss();

                    }
                });


                dialog.show();

            }
        });

        //Run the background thread
        requestMaker.runThread();


        super.onCreate(savedInstanceState);
    }


    //Parse json data

    @org.jetbrains.annotations.NotNull
    private VideoDetailsDatabase parseSingleVideoDatabase(@NonNull String input){

        VideoDetailsDatabase result = new VideoDetailsDatabase();

        //Let's start with try / catch as there are might be wrong input exists
        try {

            //First we need to get input and create a jsonObject based on input
            JSONObject jsonObject = new JSONObject(input);

            //Get Json Array from root object
            JSONArray jsonArray = jsonObject.getJSONArray(Variables.VIDEO_DETAILS_JSON_ROOT);

            //Okay strait forward :) we will then get simply objects and elements from root
            // Create a json child object again and get from index 0(Starting point)
            JSONObject childObject = jsonArray.getJSONObject(0);

            //Wow, lets time for try / catch as there are might be no object exists or
            //Object with wrong key, which may cause crash

            try{
                String title = childObject.getString(Variables.VIDEO_DETAILS_TITLE);
                String description = childObject.getString(Variables.VIDEO_DETAILS_DESCRIPTION);
                String videoURL = childObject.getString(Variables.VIDEO_DETAILS_VIDEO_URL);
                String authorName = childObject.getString(Variables.VIDEO_DETAILS_AUTHOR_NAME);
                String authorAvatarURL = childObject.getString(Variables.VIDEO_DETAILS_AUTHOR_AVATAR);
                String websiteReference = childObject.getString(Variables.VIDEO_DETAILS_WEBSITE_URL);
                String videoThumbnail = childObject.getString(Variables.VIDEO_JSON_THUMBNAIL);

                //Finally add them to database
                result = new VideoDetailsDatabase(title, description, authorName, authorAvatarURL, videoURL, videoThumbnail, websiteReference);

                //return database
                return result;
            }

            catch (Exception exception){

                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }

                //Create alertDialog saying that actual error happened
                NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                dialog.setDialogDescription(exception.toString());
                dialog.setThumbnailByResource(R.drawable.error_500);
                dialog.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClick(View view) {

                    }

                    @Override
                    public void onRightButtonClick(View view) {

                        dialog.dismiss();

                    }
                });

              
                dialog.show();

                return result;
            }


        }catch (Exception exception){

            if (kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }

            //Create alertDialog saying that actual error happened
            NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
            dialog.setDialogDescription(exception.toString());
            dialog.setThumbnailByResource(R.drawable.error_500);
            dialog.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                @Override
                public void onLeftButtonClick(View view) {

                }

                @Override
                public void onRightButtonClick(View view) {

                    dialog.dismiss();

                }
            });


            dialog.show();

            return result;

        }

    }
}
