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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrahimhossain.app.R;
import com.ibrahimhossain.app.VideoData;
import com.ibrahimhossain.app.WebRequestMaker.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    Context context;
    List<VideoData> database;
    RecyclerView recyclerView;
    VideoAdapter adapter;

    public VideoFragment(Context context, List<VideoData> videoDataList){

        this.context = context;
        this.database = videoDataList;

    }

    public VideoFragment(){


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.video_main_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.main_recycler_view);

        //Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // create new instance of Recycleview adapter
        adapter = new VideoAdapter(context, database);

        //Set adapter
        recyclerView.setAdapter(adapter);

        return rootView;


    }

    public void updateLayout(String searchedText){

        filter(searchedText);


    }





    private void filter(String text) {
        // creating a new array list to filter our data.
        List<VideoData> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (VideoData item : database) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

}
