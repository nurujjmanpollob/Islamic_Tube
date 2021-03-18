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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorDetailsActivity extends AppCompatActivity {

    AppCompatButton messageMe;
    CircleImageView authorPic;
    MaterialTextView authorNameView;
    MaterialTextView authorAddressView;
    ImageFilterView facebookView;
    ImageFilterView whatsappView;
    ImageFilterView linkedInView;
    AuthorProfileDetailsDatabase database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

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


        setContentView(R.layout.video_author_view);

        //Initialize view
        messageMe = findViewById(R.id.video_author_view_contact_button);
        authorPic = findViewById(R.id.video_author_view_image);
        authorNameView = findViewById(R.id.video_author_view_title);
        authorAddressView = findViewById(R.id.video_author_view_description);
        facebookView = findViewById(R.id.video_author_view_facebook_button);
        whatsappView = findViewById(R.id.video_author_view_whatsapp_button);
        linkedInView = findViewById(R.id.video_author_view_linkedin_button);

        if(getIntent().hasExtra(Variables.AUTHOR_PROFILE_INTENT_KEY)){

            database = getIntent().getParcelableExtra(Variables.AUTHOR_PROFILE_INTENT_KEY);
        }


        if (database != null) {
            authorNameView.setText(database.getName());
        }
        if (database != null) {
            authorAddressView.setText(database.getAddress());
        }

        messageMe.setOnClickListener(v -> {



            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",database.getEmail(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi, "+database.getName());
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your profile is great and you having amazing collection! I personally went to meet you in a particular matter and this is...");
            emailIntent.putExtra(Intent.EXTRA_STREAM, false);
            if (emailIntent.resolveActivity(getPackageManager()) != null) {

                startActivity(Intent.createChooser(emailIntent,"Choose your mail application"));

            } else {

                Toast.makeText(AuthorDetailsActivity.this,"No email App installed, install Gmail from Playstore First",Toast.LENGTH_LONG).show();
            }



        });



        whatsappView.setOnClickListener(v -> {
            //react on whatsapp view
            String contact = null; // use country code with your phone number
            if (database != null) {
                contact = database.getWhatsappNo();
            }
            String url = "https://api.whatsapp.com/send?phone=" + contact;
            try {
                PackageManager pm = AuthorDetailsActivity.this.getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(AuthorDetailsActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });


        linkedInView.setOnClickListener(v -> {
            //React on linkedIN view
            Intent i = new Intent(AuthorDetailsActivity.this, WebReferenceLoader.class);
            i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, database.getLinkedinID());
            startActivity(i);

        });

        facebookView.setOnClickListener(v -> {
            //React on Facebook View
            Intent in = new Intent(AuthorDetailsActivity.this, WebReferenceLoader.class);
            in.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, database.getFacebooklink());
            startActivity(in);

        });


        Glide.with(AuthorDetailsActivity.this).asBitmap().placeholder(R.drawable.loading_placeholder).load(database.getAvatarURL()).into(authorPic);

        super.onCreate(savedInstanceState);
    }
}
