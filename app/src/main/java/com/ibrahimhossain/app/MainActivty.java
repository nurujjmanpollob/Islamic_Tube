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


import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ibrahimhossain.app.WebRequestMaker.VideoAdapter;
import com.ibrahimhossain.app.fragments.LibraryFragment;
import com.ibrahimhossain.app.fragments.SubscriptionFragment;
import com.ibrahimhossain.app.fragments.VideoFragment;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivty extends AppCompatActivity {

   // RecyclerView recyclerView;
    Toolbar toolbar;
    CircleImageView profileImageView;

   KProgressHUD progressHUD;

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


        //Set Layout manager
      //  recyclerView.setLayoutManager(new LinearLayoutManager(MainActivty.this));


        //Call new instance of video adapter
    //   adapter  = new VideoAdapter(MainActivty.this, input);

        //set recyclerview adapter
      //  recyclerView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(isVideoFragmentVisible){

                    videoFragment.updateLayout(query);

                }
              //  filter(query);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

             //   filter(newText);
                return false;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.home_activity){

                  if(!isVideoFragmentVisible) {
                      loadFragment(new VideoFragment(MainActivty.this, input));
                  }

                    isVideoFragmentVisible = true;

                }

                if(item.getItemId() == R.id.library_activity){

                    isVideoFragmentVisible = false;

                        loadFragment(new LibraryFragment());

                }

                if(item.getItemId() == R.id.subscription_activity){

                    isVideoFragmentVisible = false;

                        loadFragment(new SubscriptionFragment());

                    return true;
                }
                return true;
            }
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

        if(!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_fragment_holder, fragment);
            transaction.commit();
        }
        }

}
