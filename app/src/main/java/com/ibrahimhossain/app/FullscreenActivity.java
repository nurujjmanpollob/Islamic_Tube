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
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ibrahimhossain.app.BackgroundWorker.JSONParser;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FullscreenActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    ImageFilterView imageFilterView;

    KProgressHUD kProgressHUD;


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

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {



            }

            @Override
            public void onAnimationEnd(Animation animation) {


                if(getIntent().hasExtra(Variables.FIREBASE_NOTIFICATION_KEY_URL)){

                    String url = getIntent().getStringExtra(Variables.WEB_REFERENCE_INTENT_KEY);

                    Intent i = new Intent(FullscreenActivity.this, WebReferenceLoader.class);
                    i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, url);
                    startActivity(i);


                }else {


                    WebRequestMaker requestMaker = new WebRequestMaker(Variables.HOME_JSON_URL);
                    requestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {
                        @Override
                        public void onStartExecuting() {

                            kProgressHUD = new KProgressHUD(FullscreenActivity.this);
                            kProgressHUD.setCancellable(false);
                            kProgressHUD.setLabel("Connecting to database...");
                            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                            kProgressHUD.show();

                        }

                        @Override
                        public void onTaskFinished(String result) {

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }

                            JSONParser parser = new JSONParser(result, Variables.VIDEO_JSON_ROOT);
                            parser.setListener(new JSONParser.OnJSONParseEvent() {
                                @Override
                                public void onSuccessfullyParse(List<VideoData> videoDataList) {

                                    Intent intent = new Intent(FullscreenActivity.this, MainActivty.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList(Variables.VIDEO_INTENT_KEY, (ArrayList<? extends Parcelable>) videoDataList);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    finish();

                                }

                                @Override
                                public void onNullInput() {


                                }

                                @Override
                                public void containsErrorInArray(String cause, List<VideoData> dataParsedSoFar) {

                                    NJPollobDialogLayout layout = new NJPollobDialogLayout(FullscreenActivity.this);
                                    layout.setDialogDescription(cause);
                                    layout.setThumbnailByResource(R.drawable.error_404);
                                    layout.setCancelable(false);
                                    layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                                        @Override
                                        public void onLeftButtonClick(View view) {

                                        }

                                        @Override
                                        public void onRightButtonClick(View view) {

                                            finish();

                                        }
                                    });

                                    layout.show();


                                }

                                @Override
                                public void singleItemFailed(String cause, List<VideoData> dataParsedSoFar) {

                                    NJPollobDialogLayout layout = new NJPollobDialogLayout(FullscreenActivity.this);
                                    layout.setDialogDescription(cause);
                                    layout.setThumbnailByResource(R.drawable.error_404);
                                    layout.setCancelable(false);
                                    layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                                        @Override
                                        public void onLeftButtonClick(View view) {

                                        }

                                        @Override
                                        public void onRightButtonClick(View view) {

                                            finish();

                                        }
                                    });

                                    layout.show();

                                }
                            });

                            parser.Parse();


                        }

                        @Override
                        public void onLoadFailed(String cause) {

                            if (kProgressHUD.isShowing()) {
                                kProgressHUD.dismiss();
                            }

                            NJPollobDialogLayout layout = new NJPollobDialogLayout(FullscreenActivity.this);
                            layout.setDialogDescription(cause);
                            layout.setThumbnailByResource(R.drawable.error_404);
                            layout.setCancelable(false);
                            layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                                @Override
                                public void onLeftButtonClick(View view) {

                                }

                                @Override
                                public void onRightButtonClick(View view) {

                                    finish();

                                }
                            });

                            layout.show();


                        }
                    });
                    requestMaker.runThread();


                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });






    }





}