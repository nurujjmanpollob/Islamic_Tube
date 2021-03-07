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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {



            NJPollobDialogLayout njPollobDialogLayout = new NJPollobDialogLayout(FullscreenActivity.this);
            njPollobDialogLayout.setDialogDescription("There are test is now going :) Tap yes to run this app!");
            njPollobDialogLayout.setListenerOnDialogButtonClick("Run", "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                @Override
                public void onLeftButtonClick(View view) {

                    startActivity(new Intent(FullscreenActivity.this, VideoDetails.class));


                }

                @Override
                public void onRightButtonClick(View view) {

                    Snackbar sb = Snackbar.make(constraintLayout, "You pressed Close button", 5000);
                    sb.show();

                }
            });

            njPollobDialogLayout.show();





        }, 2200);



    }





}