package com.ibrahimhossain.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.ibrahimhossain.app.WebRequestMaker.CheckConnection;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;

import java.util.Objects;


public class FullscreenActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    ImageFilterView imageFilterView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set full screen mode
        if(Objects.requireNonNull(getSupportActionBar()).isShowing()){

            getSupportActionBar().hide();
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //Set content view
        setContentView(R.layout.activity_fullscreen);


        imageFilterView = findViewById(R.id.splash_screen_logo);
        constraintLayout = findViewById(R.id.main_splash_constraint);

        //Create animation and apply to Image view
        Animation anim = AnimationUtils.loadAnimation(FullscreenActivity.this, R.anim.top_animation);

        imageFilterView.setAnimation(anim);

        //Run basic connectivity check

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {



                NJPollobDialogLayout njPollobDialogLayout = new NJPollobDialogLayout(FullscreenActivity.this);
                njPollobDialogLayout.setDialogDescription("There are test is now going :) Tap yes to run this app!");
                njPollobDialogLayout.setListenerOnDialogButtonClick("Run", "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClick(View view) {

                        startActivity(new Intent(FullscreenActivity.this, VideoPlayerView.class));

                    }

                    @Override
                    public void onRightButtonClick(View view) {

                        Snackbar sb = Snackbar.make(constraintLayout, "You pressed Close button", 5000);
                        sb.show();

                    }
                });

                njPollobDialogLayout.show();





            }
        }, 2200);



    }





}