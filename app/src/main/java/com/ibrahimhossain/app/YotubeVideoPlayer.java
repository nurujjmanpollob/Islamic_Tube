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

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ibrahimhossain.app.BackgroundWorker.YoutubeAPIActivity;

import org.jetbrains.annotations.NotNull;

public class YotubeVideoPlayer extends YoutubeAPIActivity implements YouTubePlayer.OnFullscreenListener {

    private static final int PORTRAIT_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    private YouTubePlayerView playerView;
    private YouTubePlayer player;


    @Override
    protected void onCreate(Bundle bundle) {
        setContentView(R.layout.yotube_player_view);

        LinearLayoutCompat baseLayout = findViewById(R.id.youtube_player_view_main_root);
        playerView = findViewById(R.id.youtube_player_view_main);

        //Initialize the player
        playerView.initialize(Variables.YOUTUBE_PLAYER_DEVELOPER_KEY, this);


        doLayout();

        super.onCreate(bundle);
    }



    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onFullscreen(boolean b) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.player = youTubePlayer;
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!b) {
            player.cueVideo("avP5d16wEp0");
        }

    }



    private void doLayout() {
        LinearLayoutCompat.LayoutParams playerParams =
                (LinearLayoutCompat.LayoutParams) playerView.getLayoutParams();

            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayoutCompat.LayoutParams.MATCH_PARENT;



        int controlFlags = player.getFullscreenControlFlags();

            // If you use the FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE, your activity's normal UI
            // should never be laid out in landscape mode (since the video will be fullscreen whenever the
            // activity is in landscape orientation). Therefore you should set the activity's requested
            // orientation to portrait. Typically you would do this in your AndroidManifest.xml, we do it
            // programmatically here since this activity demos fullscreen behavior both with and without
            // this flag).
            setRequestedOrientation(PORTRAIT_ORIENTATION);
            controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;


        player.setFullscreenControlFlags(controlFlags);

        }



    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    }





