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
import android.os.Handler;
import android.os.Looper;
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
import com.ibrahimhossain.app.BackgroundWorker.InternetImageLoader;
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

    //Create field for database url
   // String databaseURL;

    //Create progress drawable;
    KProgressHUD kProgressHUD;

    // Error View
    View errorView;

    //Main ScrollView
    ScrollView mainView;


    //Get direct database
    VideoDetailsDatabase videoDetailsDatabase;

    //Main play button
    ImageFilterView playButton;


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

        //Check for intent data that passed from previous Activity
        if(getIntent().hasExtra(Variables.VIDEO_DETAILS_INTENT_KEY)){

            //assign database url
            videoDetailsDatabase = getIntent().getParcelableExtra(Variables.VIDEO_DETAILS_INTENT_KEY);

        }

        //Setting values
        if (videoDetailsDatabase != null) {
            videoTitleView.setText(videoDetailsDatabase.getVideoTitle());
        }
        if (videoDetailsDatabase != null) {
            videoDescriptionView.setText(videoDetailsDatabase.getVideoDescription());
        }
        if (videoDetailsDatabase != null) {
            authorNameView.setText(videoDetailsDatabase.getAuthorName());
        }

        //Set thumbnail Image
        if (videoDetailsDatabase != null) {
            new InternetImageLoader(videoDetailsDatabase.getVideoThumbnail(), R.drawable.error_404, thumbnailView, VideoDetails.this).runThread();
        }

        //Set Avatar Image and set
        if (videoDetailsDatabase != null) {
            new InternetImageLoader(videoDetailsDatabase.getAuthorAvatarURL(), 0, authorAvatarView, VideoDetails.this).runThread();
        }




        //React on websiteView
        websiteView.setOnClickListener(v -> {
            try {
                if (URLUtil.isValidUrl(videoDetailsDatabase.getVideoReferenceWebsite())) {
                    Intent i = new Intent(VideoDetails.this, WebReferenceLoader.class);
                    i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, videoDetailsDatabase.getVideoReferenceWebsite());
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
                intent.setDataAndType(Uri.parse(videoDetailsDatabase.getVideoURL()), "video/*");
                startActivity(intent);


            }catch (Exception ess){

                Toast.makeText(VideoDetails.this, "This video cant be shared!", Toast.LENGTH_LONG).show();

            }
        });

        //React on play button or thumbnail view
        playButton.setOnClickListener(v -> {


            IsValidYoutubeURL videoURLS = new IsValidYoutubeURL(videoDetailsDatabase.getVideoURL());
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
