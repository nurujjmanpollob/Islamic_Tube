package com.ibrahimhossain.app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

public class VideoPlayerView extends AppCompatActivity {


    private static String videoURL = "";


    private VideoView mVideoPlayerView;
 //   private ProgressDialog pDialog;

    KProgressHUD kProgressHUD;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    // Root view's LayoutParams
    private FrameLayout.LayoutParams mRootParam;

    // detector to pinch zoom in/out
    private ScaleGestureDetector mScaleGestureDetector;

    // detector to single tab
    private GestureDetector mGestureDetector;

    FrameLayout rootView;
    MediaController controller;







    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        hideSystemUI();


        if(getIntent().hasExtra(Variables.VIDEO_INTENT_KEY)){

            videoURL = getIntent().getStringExtra(Variables.VIDEO_INTENT_KEY);
        }


        mRootParam = (FrameLayout.LayoutParams) findViewById(R.id.video_playerFrameLayout).getLayoutParams();
        rootView = findViewById(R.id.video_playerFrameLayout);


        // set up gesture listeners
        mScaleGestureDetector = new ScaleGestureDetector(this, (ScaleGestureDetector.OnScaleGestureListener) new MyScaleGestureListener());
        mGestureDetector = new GestureDetector(this, (GestureDetector.OnGestureListener) new MySimpleOnGestureListener());

        /*
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Video from NJPS Entertainmemt Server");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pDialog.create();
        }
        pDialog.show();
        pDialog.setCancelable(false);

         */

        kProgressHUD = new KProgressHUD(this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        kProgressHUD.setLabel("Video is now buffering...");
        kProgressHUD.setCancellable(false);
        kProgressHUD.show();

        mVideoPlayerView = findViewById(R.id.video_player_view);


        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }


        // Set up the media controller widget and attach it to the video view.
        controller    = new MediaController(this);
        controller.setMediaPlayer(mVideoPlayerView);
        mVideoPlayerView.setMediaController(controller);
        controller.show();



    }


    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < 24) {
            mVideoPlayerView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoPlayerView.getCurrentPosition());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializePlayer() {


        Uri uri = null;

        if(videoURL == null|| videoURL.equals("")){


            NJPollobDialogLayout layout = new NJPollobDialogLayout(VideoPlayerView.this);
            layout.setDialogDescription("It is looking this requested video is not exists in our server!");
            layout.setListenerOnDialogButtonClick("Okay", null, new NJPollobDialogLayout.DialogButtonClickListener() {
                @Override
                public void onLeftButtonClick(View view) {

                    VideoPlayerView.this.finish();

                }

                @Override
                public void onRightButtonClick(View view) {

                    VideoPlayerView.this.finish();

                }
            });

            layout.show();

        }else{

            if(URLUtil.isValidUrl(videoURL)){
                uri = Uri.parse(videoURL);

            }else{

                Toast.makeText(VideoPlayerView.this,
                        "It seems this video URL can't be parsed, please report.",
                        Toast.LENGTH_LONG)
                        .show();
            }


        }
        mVideoPlayerView.setVideoURI(uri);


        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoPlayerView.setOnPreparedListener(
                mediaPlayer -> {



                 kProgressHUD.dismiss();

                    // Restore saved position, if available.
                    if (mCurrentPosition > 0) {
                        mVideoPlayerView.seekTo(mCurrentPosition);
                    } else {
                        // Skipping to 1 shows the first frame of the video.
                        mVideoPlayerView.seekTo(1);
                    }

                    // Start playing!
                    mVideoPlayerView.start();
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoPlayerView.setOnCompletionListener(
                mediaPlayer -> {

                    // Return the video position to the start.
                    mVideoPlayerView.seekTo(0);

                });

        mVideoPlayerView.setOnErrorListener((p1, p2, p3) -> {
        kProgressHUD.dismiss();

            return false;
        });



        mVideoPlayerView.setOnTouchListener((v, event) -> {
            mGestureDetector.onTouchEvent(event);
            mScaleGestureDetector.onTouchEvent(event);
            return true;
        });




    }




    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        mVideoPlayerView.stopPlayback();
    }


    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (mVideoPlayerView == null)
                return false;
            if(controller.isShowing()){
                controller.hide();
            }else{
                controller.show();
            }

            return true;
        }

    }



    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private int mW, mH;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // scale our video view
            mW *= detector.getScaleFactor();
            mH *= detector.getScaleFactor();
            int MIN_WIDTH = 100;
            if (mW < MIN_WIDTH) { // limits width
                mW = mVideoPlayerView.getWidth();
                mH = mVideoPlayerView.getHeight();
            }
            mVideoPlayerView.getHolder().setFixedSize(mW, mH);
            mRootParam.width = mW;
            mRootParam.height = mH;

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mW = mVideoPlayerView.getWidth();
            mH = mVideoPlayerView.getHeight();

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {


        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY

		/*
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			*/
        // Set the content to appear under the system bars so that the
        // content doesn't resize when the system bars hide and show.
        //      | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //       | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // Hide the nav bar and status bar
         /*   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN);
			*/
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }




}
