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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ibrahimhossain.app.fragments.LibraryFragment;
import com.ibrahimhossain.app.fragments.SubscriptionFragment;
import com.ibrahimhossain.app.fragments.VideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;
import abhishekti7.unicorn.filepicker.utils.Constants;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivty extends AppCompatActivity {

   // RecyclerView recyclerView;
    Toolbar toolbar;
    CircleImageView profileImageView;

    View errorTxt;



    //Add database instance
    List<VideoData> input = new ArrayList<>();

    //Searchview
    SearchView searchView;

    //Bottom Navigation view
    BottomNavigationView bottomNavigationView;

    //Video Fragment
    VideoFragment videoFragment;

    //save video fragment state(Visibity) in boolean
    Boolean isVideoFragmentVisible = true;


    private final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };




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
       // recyclerView = findViewById(R.id.main_recycler_view);
        errorTxt = findViewById(R.id.main_error_text_view);
        searchView = findViewById(R.id.main_toolbar_search_view);
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation_view);

        if(getIntent().hasExtra(Variables.VIDEO_INTENT_KEY)){

            input = getIntent().getParcelableArrayListExtra(Variables.VIDEO_INTENT_KEY);
        }

        videoFragment = new VideoFragment(MainActivty.this, input);
        //Load main fragment
        loadFragment(videoFragment);


        //Check if user have set their profile picture
        SharedPreferences preferences = MainActivty.this.getSharedPreferences(Variables.USER_IMAGE_URI_PREF, Context.MODE_PRIVATE);
        String imgDir = preferences.getString(Variables.USER_IMAGE_DIRECTORY, null);
        if(imgDir != null){

            profileImageView.setImageURI(Uri.fromFile(new File(imgDir)));

        }


        closeKeyboard();

        if(!allPermissionsGranted()){

            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(isVideoFragmentVisible){

                    videoFragment.updateLayout(query);



                }

                closeKeyboard();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home_activity){

              if(!isVideoFragmentVisible) {
                  loadFragment(new VideoFragment(MainActivty.this, input));
              }

                isVideoFragmentVisible = true;

            }

            if(item.getItemId() == R.id.library_activity){

                isVideoFragmentVisible = false;

                    loadFragment(new LibraryFragment(MainActivty.this));

            }

            if(item.getItemId() == R.id.subscription_activity){

                isVideoFragmentVisible = false;

                    loadFragment(new SubscriptionFragment(MainActivty.this));

                return true;
            }
            return true;
        });


        profileImageView.setOnClickListener(v -> {

            UnicornFilePicker.from(MainActivty.this)
                    .addConfigBuilder()
                    .selectMultipleFiles(false)
                    .setRootDirectory(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .showHiddenFiles(false)
                    .setFilters(new String[]{"png", "jpg", "jpeg"})
                    .addItemDivider(true)
                    .theme(R.style.UnicornFilePicker_Default)
                    .build()
                    .forResult(Constants.REQ_UNICORN_FILE);

        });



    }





    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



    public void loadFragment(Fragment fragment) {


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fragment_holder, fragment);
            transaction.commit();


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == Constants.REQ_UNICORN_FILE && resultCode == RESULT_OK){

            //get uri
            if (data != null ) {

                ArrayList<String> files = data.getStringArrayListExtra("filePaths");
                String imageDIR = null;
                if (files != null) {
                    imageDIR = files.get(0);
                }

                SharedPreferences preferences = MainActivty.this.getSharedPreferences(Variables.USER_IMAGE_URI_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Variables.USER_IMAGE_DIRECTORY, imageDIR);
                editor.apply();

                if (imageDIR != null) {
                    profileImageView.setImageURI(Uri.fromFile(new File(imageDIR)));
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(MainActivty.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void closeKeyboard() {


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

}
