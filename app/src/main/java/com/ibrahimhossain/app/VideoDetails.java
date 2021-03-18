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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
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

import com.bumptech.glide.Glide;
import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibrahimhossain.app.BackgroundWorker.IsValidYoutubeURL;
import com.ibrahimhossain.app.BackgroundWorker.JSONParser;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
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

    //Create field for database url
   // String databaseURL;

    //Create progress drawable;
    KProgressHUD kProgressHUD;

    // Error View
    View errorView;

    //Main ScrollView
    ScrollView mainView;


    //Get direct database in String format
    String database;

    //Main play button
    ImageFilterView playButton;


    //get video URL;
    String videoURL;

    //get website reference
    String websiteReference;

    //String author profile URL;
    String authorProfileURL;

    //Favorite video button;
    ImageFilterView favVideoButton;


    //get video title
    String videoTitle;


    String videoDetailsURL;


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
        playButton = findViewById(R.id.video_details_main_play_view);
        favVideoButton = findViewById(R.id.video_details_bookmarked_video);

        //Check for intent data that passed from previous Activity
        if(getIntent().hasExtra(Variables.VIDEO_DETAILS_INTENT_KEY) && getIntent().hasExtra(Variables.VIDEO_DETAILS_URL_RAW_KEY)){

            //get database
           database = getIntent().getStringExtra(Variables.VIDEO_DETAILS_INTENT_KEY);
           videoDetailsURL = getIntent().getStringExtra(Variables.VIDEO_DETAILS_URL_RAW_KEY);

        }


        //Parse current database


        JSONParser.VideoDetailsParser videoDetailsParser = new JSONParser.VideoDetailsParser();
        videoDetailsParser.ModeVideoDetailsDatabase(Variables.VIDEO_DETAILS_JSON_ROOT, database);
        videoDetailsParser.setListenerForVideoDetailsDatabase(new JSONParser.VideoDetailsParser.VideoDetailsDatabaseListener() {
            @Override
            public void onNullValueOrInput() {


            }

            @Override
            public void onArrayNotFound(String cause) {


                NJPollobDialogLayout layout = new NJPollobDialogLayout(VideoDetails.this);
                layout.setDialogDescription(cause);
                layout.setCancelable(false);
                layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClick(View view) {

                    }

                    @Override
                    public void onRightButtonClick(View view) {

                        layout.dismiss();


                    }
                });

                layout.show();



            }

            @Override
            public void onSingleObjectNotFound(String cause) {

                NJPollobDialogLayout layout = new NJPollobDialogLayout(VideoDetails.this);
                layout.setDialogDescription(cause);
                layout.setCancelable(false);
                layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClick(View view) {

                    }

                    @Override
                    public void onRightButtonClick(View view) {

                        layout.dismiss();


                    }
                });

                layout.show();

            }

            @Override
            public void onSuccessfulData(VideoDetailsDatabase videoDetailsDatabase) {

                videoTitleView.setText(videoDetailsDatabase.getVideoTitle());
                videoDescriptionView.setText(videoDetailsDatabase.getVideoDescription());
                authorNameView.setText(videoDetailsDatabase.getAuthorName());

                Glide.with(VideoDetails.this).asBitmap().placeholder(R.drawable.loading_placeholder).load(videoDetailsDatabase.getVideoThumbnail()).into(thumbnailView);

                Glide.with(VideoDetails.this).asBitmap().placeholder(R.drawable.loading_placeholder).load(videoDetailsDatabase.getAuthorAvatarURL()).into(authorAvatarView);

                videoURL = videoDetailsDatabase.getVideoURL();
                websiteReference = videoDetailsDatabase.getVideoReferenceWebsite();
                videoTitle = videoDetailsDatabase.getVideoTitle();
                authorProfileURL = videoDetailsDatabase.getAuthorProfileURL();
            }
        });

        videoDetailsParser.parseVideoDatabaseForResult();


        //React on websiteView
        websiteView.setOnClickListener(v -> {
            try {
                if (URLUtil.isValidUrl(websiteReference)) {
                    Intent i = new Intent(VideoDetails.this, WebReferenceLoader.class);
                    i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, websiteReference);
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

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, videoURL);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "URL");

                startActivity(Intent.createChooser(shareIntent, "Share with your friends"));


            }catch (Exception ess){

                Toast.makeText(VideoDetails.this, "This video cant be shared!", Toast.LENGTH_LONG).show();

            }
        });

        //React on play button or thumbnail view
        playButton.setOnClickListener(v -> {


            IsValidYoutubeURL videoURLS = new IsValidYoutubeURL(videoURL);
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

            videoURLS.runTest();



        });

        //React on Author avatar view
        authorAvatarView.setOnClickListener(v -> {

            WebRequestMaker requestMaker = new WebRequestMaker(authorProfileURL);
            requestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {

                KProgressHUD kProgressHUD;
                @Override
                public void onStartExecuting() {

                    kProgressHUD = new KProgressHUD(VideoDetails.this);
                    kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                    kProgressHUD.setLabel("Querying Author Profile...");
                    kProgressHUD.setCancellable(false);
                    kProgressHUD.show();

                }

                @Override
                public void onTaskFinished(String result) {

                    if(kProgressHUD.isShowing()){

                        kProgressHUD.dismiss();
                    }
                    AuthorProfileDetailsParser parser = new AuthorProfileDetailsParser(Variables.AUTHOR_PROFILE_DATABASE_ROOT, result);
                    parser.setListener(new AuthorProfileDetailsParser.AuthorProfileDetailsDatabaseListener() {
                        @Override
                        public void onNullValueOrInput() {

                        }

                        @Override
                        public void onArrayNotFound(String cause) {

                            NJPollobDialogLayout layout = new NJPollobDialogLayout(VideoDetails.this);
                            layout.setDialogDescription(cause);
                            layout.setCancelable(false);
                            layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                                @Override
                                public void onLeftButtonClick(View view) {

                                }

                                @Override
                                public void onRightButtonClick(View view) {

                                    layout.dismiss();


                                }
                            });

                            layout.show();
                        }

                        @Override
                        public void onSingleObjectNotFound(String cause) {

                            NJPollobDialogLayout layout = new NJPollobDialogLayout(VideoDetails.this);
                            layout.setDialogDescription(cause);
                            layout.setCancelable(false);
                            layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                                @Override
                                public void onLeftButtonClick(View view) {

                                }

                                @Override
                                public void onRightButtonClick(View view) {

                                    layout.dismiss();


                                }
                            });

                            layout.show();

                        }

                        @Override
                        public void onSuccessfulData(AuthorProfileDetailsDatabase videoDetailsDatabase) {

                            Intent i = new Intent(VideoDetails.this, AuthorDetailsActivity.class);
                            Bundle v = new Bundle();
                            i.putExtra(Variables.AUTHOR_PROFILE_INTENT_KEY, videoDetailsDatabase);
                            i.putExtras(v);
                            startActivity(i);

                        }
                    });

                    parser.parseVideoDatabaseForResult();

                }

                @Override
                public void onLoadFailed(String cause) {

                    if(kProgressHUD.isShowing()){

                        kProgressHUD.dismiss();
                    }

                }
            });

            requestMaker.runThread();

        });



        // Check if current video is in favorite video list
        SharedPreferences sharedPreferences = getSharedPreferences(Variables.FAVORITE_VIDEO_DATASHEET_NAME, Context.MODE_PRIVATE);
        String links = sharedPreferences.getString(Variables.FAVORITE_VIDEO_URL, null);

        //check for database entry
        if(links != null){

            //create instance of Gson
            Gson gson = new Gson();

            List<String> videoList = gson.fromJson(links, new TypeToken<List<String>>() {
            }.getType());

            //check if current video is exists in videoList or not
            if(videoList.contains(videoDetailsURL)){
                favVideoButton.setImageResource(R.drawable.ic_baseline_bookmarked);
            }else {

                favVideoButton.setImageResource(R.drawable.ic_baseline_no_bookmark);
            }



        }else {

            favVideoButton.setImageResource(R.drawable.ic_baseline_no_bookmark);
        }


        //Favorite video button view
        favVideoButton.setOnClickListener(v -> {


            SharedPreferences sharedPreferences1 = getSharedPreferences(Variables.FAVORITE_VIDEO_DATASHEET_NAME, Context.MODE_PRIVATE);
            String jsonLink = sharedPreferences1.getString(Variables.FAVORITE_VIDEO_URL, null);
            String jsonTitle = sharedPreferences1.getString(Variables.FAVORITE_VIDEO_TITLE, null);


            if (jsonLink != null && jsonTitle != null) {

                Gson gson = new Gson();
                List<String> linkList = gson.fromJson(jsonLink, new TypeToken<List<String>>() {
                }.getType());

                List<String> titleList = gson.fromJson(jsonTitle, new TypeToken<List<String>>() {
                }.getType());

                if (linkList.contains(videoDetailsURL)) {

                    linkList.remove(videoDetailsURL);
                    titleList.remove(videoTitle);

                    SharedPreferences.Editor editor = sharedPreferences1.edit();

                    editor.putString(Variables.FAVORITE_VIDEO_URL, new Gson().toJson(linkList));
                    editor.putString(Variables.FAVORITE_VIDEO_TITLE, new Gson().toJson(titleList));


                    editor.apply();

                    favVideoButton.setImageResource(R.drawable.ic_baseline_no_bookmark);

                    Toast.makeText(VideoDetails.this, "Bookmark Removed", Toast.LENGTH_SHORT).show();


                } else {
                    linkList.add(videoDetailsURL);
                    titleList.add(videoTitle);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString(Variables.FAVORITE_VIDEO_URL, new Gson().toJson(linkList));
                    editor.putString(Variables.FAVORITE_VIDEO_TITLE, new Gson().toJson(titleList));
                    editor.apply();

                    Toast.makeText(VideoDetails.this, "Bookmarked", Toast.LENGTH_SHORT).show();

                    favVideoButton.setImageResource(R.drawable.ic_baseline_bookmarked);

                }
            } else {

                List<String> linkList = new ArrayList<>();
                List<String> titleList = new ArrayList<>();
                linkList.add(videoDetailsURL);
                titleList.add(videoTitle);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(Variables.FAVORITE_VIDEO_URL, new Gson().toJson(linkList));
                editor.putString(Variables.FAVORITE_VIDEO_TITLE, new Gson().toJson(titleList));
                editor.apply();

                favVideoButton.setImageResource(R.drawable.ic_baseline_bookmarked);

                Toast.makeText(VideoDetails.this, "Bookmarked", Toast.LENGTH_SHORT).show();
            }





        });


        super.onCreate(savedInstanceState);
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
