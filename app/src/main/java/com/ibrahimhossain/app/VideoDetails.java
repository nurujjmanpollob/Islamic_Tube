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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.google.android.material.textview.MaterialTextView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class VideoDetails extends AppCompatActivity {


    // create field for Video thumbnail view
    GifImageView thumbnailView;

    //Create field for Thumbnail view Root layout
    ImageFilterView thumbnailViewRootRelative;

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





}
