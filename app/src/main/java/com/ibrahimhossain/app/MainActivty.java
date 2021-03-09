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
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

   KProgressHUD progressHUD;

    View errorTxt;


    //Add database instance
    List<VideoData> input = new ArrayList<>();


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

        if(getIntent().hasExtra(Variables.VIDEO_INTENT_KEY)){

            input = getIntent().getParcelableArrayListExtra(Variables.VIDEO_INTENT_KEY);
        }

        //Set Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivty.this));


        //Call new instance of video adapter
        VideoAdapter adapter = new VideoAdapter(MainActivty.this, input);

        //set recyclerview adapter
        recyclerView.setAdapter(adapter);




    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
