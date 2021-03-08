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

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.ibrahimhossain.app.BackgroundWorker.IsValidYoutubeURL;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.dialogview.CacheUriPerser;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.ibrahimhossain.app.dialogview.NJPollobDialogWorker;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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

    // Error View
    View errorView;

    //Main ScrollView
    ScrollView mainView;


    //request mode

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;


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
        errorView = findViewById(R.id.video_details_main_error_view);
        mainView = findViewById(R.id.video_details_view_main_root_scrollview);

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
            public void onTaskFinished(String result) {

                //get database
                database = parseSingleVideoDatabase(result);

                //Check is database is null or not!
                if(!database.getIsNotNull()){

                    if (kProgressHUD.isShowing()) {
                        kProgressHUD.dismiss();
                    }

                    errorView.setVisibility(View.VISIBLE);
                    mainView.setVisibility(View.GONE);

                    //Create alertDialog saying that actual error happened
                    NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                    dialog.setDialogDescription("The database is not found, usually because of no internet connection.");
                    dialog.setThumbnailByResource(R.drawable.error_404);
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

                errorView.setVisibility(View.VISIBLE);
                mainView.setVisibility(View.GONE);


                //Create alertDialog saying that actual error happened
                NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                dialog.setDialogDescription(cause);
                dialog.setThumbnailByResource(R.drawable.error_404);
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



        //React on websiteView
        websiteView.setOnClickListener(v -> {
            try {
                if (URLUtil.isValidUrl(database.getVideoReferenceWebsite())) {
                    Intent i = new Intent(VideoDetails.this, WebReferenceLoader.class);
                    i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, database.getVideoReferenceWebsite());
                    startActivity(i);
                }else {

                    Toast.makeText(VideoDetails.this, "This URL is invalid", Toast.LENGTH_LONG).show();

                }
            }catch (Exception exc){


                //Create alertDialog saying that actual error happened
                NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                dialog.setDialogDescription(exc.toString());
                dialog.setThumbnailByResource(R.drawable.error_404);
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



        //React on Share Button
        shareButton.setOnClickListener(v -> {
            try{

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(database.getVideoURL()), "video/*");
                startActivity(intent);


            }catch (Exception ess){

                Toast.makeText(VideoDetails.this, "This video cant be shared!", Toast.LENGTH_LONG).show();

            }
        });

        //React on play button or thumbnail view
        thumbnailViewRootRelative.setOnClickListener(v -> {


            IsValidYoutubeURL videoURLS = new IsValidYoutubeURL(database.getVideoURL());
            videoURLS.setListenerForURLEvent(new IsValidYoutubeURL.ListenerOnURLEvent() {
                @Override
                public void invalidURL() {
                    Toast.makeText(VideoDetails.this, "this url is invalid", Toast.LENGTH_LONG).show();
                }

                @Override
                public void regularVideo(String videoURL) {

                    Intent i = new Intent(VideoDetails.this, VideoPlayerView.class);
                    i.putExtra(Variables.VIDEO_INTENT_KEY, videoURL);
                    startActivity(i);

                }

                @Override
                public void youtubeVideoKey(String key) {



                    Intent intent  = YouTubeStandalonePlayer.createVideoIntent(
                            VideoDetails.this, Variables.YOUTUBE_PLAYER_DEVELOPER_KEY, key, 1000, true, false);

                    if (intent != null) {
                        if (canResolveIntent(intent)) {
                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                        } else {
                            // Could not resolve the intent - must need to install or update the YouTube API service.
                            YouTubeInitializationResult.SERVICE_MISSING
                                    .getErrorDialog(VideoDetails.this, REQ_RESOLVE_SERVICE_MISSING).show();
                        }

                    }


                }
            });



        });


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

                errorView.setVisibility(View.VISIBLE);
                mainView.setVisibility(View.GONE);

                //Create alertDialog saying that actual error happened
                NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
                dialog.setDialogDescription(exception.toString());
                dialog.setThumbnailByResource(R.drawable.error_404);
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

            errorView.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);

            //Create alertDialog saying that actual error happened
            NJPollobDialogLayout dialog = new NJPollobDialogLayout(VideoDetails.this);
            dialog.setDialogDescription(exception.toString());
            dialog.setThumbnailByResource(R.drawable.error_404);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


        private boolean canResolveIntent(Intent intent) {
            List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
            return !resolveInfo.isEmpty();
        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {

                Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
