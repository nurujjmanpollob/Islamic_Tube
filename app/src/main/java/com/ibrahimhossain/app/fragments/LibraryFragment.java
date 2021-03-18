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

package com.ibrahimhossain.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrahimhossain.app.FavVideoDataReader;
import com.ibrahimhossain.app.FavoriteVideoAdapter;
import com.ibrahimhossain.app.FavoriteVideoDatabase;
import com.ibrahimhossain.app.R;

import java.util.List;

public class LibraryFragment extends Fragment {

   RecyclerView recyclerView;

    Handler handler;
    Runnable runnable;

  FavoriteVideoAdapter favoriteVideoAdapter;


    Context context;

    View errorView;




    public LibraryFragment(Context context){
        this.context = context;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {



        // get database

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.library_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.favorite_video_list_recycler_view);

        errorView = rootView.findViewById(R.id.favorite_video_error_text_view);



        FavVideoDataReader reader = new FavVideoDataReader(context);
        reader.setEvent(new FavVideoDataReader.OnFavVideoDataLoadEvent() {
            @Override
            public void onHasDatabase(List<FavoriteVideoDatabase> databaseList) {


                if(databaseList != null) {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        favoriteVideoAdapter = new FavoriteVideoAdapter(context, databaseList, errorView);
                        recyclerView.setAdapter(favoriteVideoAdapter);

                    });
                }else {

                    new Handler(Looper.getMainLooper()).post(() -> errorView.setVisibility(View.VISIBLE));
                }





            }

            @Override
            public void onNullDatabase() {


                new Handler(Looper.getMainLooper()).post(() -> errorView.setVisibility(View.VISIBLE));


            }
        });

        reader.runThread();






        return rootView;
    }






}
