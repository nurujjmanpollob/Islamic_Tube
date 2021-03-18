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


package com.ibrahimhossain.app.WebRequestMaker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.R;
import com.ibrahimhossain.app.Variables;
import com.ibrahimhossain.app.VideoData;
import com.ibrahimhossain.app.VideoDetails;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterView> {

  Context applicationContext;
  List<VideoData> videoData;


    public VideoAdapter(Context applicationContext, List<VideoData> dataSheet){


        this.applicationContext = applicationContext;
        this.videoData = dataSheet;


    }

    @NonNull
    @Override
    public VideoAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(applicationContext.getResources().getLayout(R.layout.card_layout_main), parent,false);

        return new VideoAdapterView(layoutView);
    }


    public void filterList(List<VideoData> dataList){

        videoData = dataList;
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterView holder, int position) {


        Glide.with(applicationContext).asBitmap().placeholder(R.drawable.loading_placeholder).load(videoData.get(position).getThumbnailURL()).into(holder.thumbnailView);

        //set title
        holder.videoTitleView.setText(videoData.get(position).getTitle());

        //Set description
        holder.videoDescriptionView.setText(videoData.get(position).getDescription());

        //Lets create some animation

        Animation animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in);

        //Set animation to root view
        holder.rootView.setAnimation(animation);


        //React on thumbnail click
        holder.thumbnailView.setOnClickListener(v -> {

            WebRequestMaker requestMaker = new WebRequestMaker(videoData.get(position).getVideoURL());
            requestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {

                KProgressHUD kProgressHUD;

                @Override
                public void onStartExecuting() {

                    kProgressHUD = new KProgressHUD(applicationContext);
                    kProgressHUD.setCancellable(false);
                    kProgressHUD.setLabel("Getting Video Information...");
                    kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                    kProgressHUD.show();

                }

                @Override
                public void onTaskFinished(String result) {


                    if(kProgressHUD.isShowing()){
                        kProgressHUD.dismiss();
                    }


                    Intent i = new Intent(applicationContext, VideoDetails.class);
                    i.putExtra(Variables.VIDEO_DETAILS_INTENT_KEY, result);
                    i.putExtra(Variables.VIDEO_DETAILS_URL_RAW_KEY, videoData.get(position).getVideoURL());
                    applicationContext.startActivity(i);


                }

                @Override
                public void onLoadFailed(String cause) {




                    if(kProgressHUD.isShowing()){
                        kProgressHUD.dismiss();
                    }


                    NJPollobDialogLayout layout = new NJPollobDialogLayout(applicationContext);
                    layout.setDialogDescription(cause);
                    layout.setCancelable(false);
                    layout.setListenerOnDialogButtonClick(null, "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                        @Override
                        public void onLeftButtonClick(View view) {

                        }

                        @Override
                        public void onRightButtonClick(View view) {

                            layout.dismiss();


                        }
                    });

                    layout.show();

                }
            });

            requestMaker.runThread();


        });






    }

    @Override
    public int getItemCount() {

        if(videoData.size() != 0){
            return videoData.size();
        }else {

            return 0;
        }

    }

     static class VideoAdapterView extends RecyclerView.ViewHolder {

        ImageFilterView thumbnailView;
        MaterialTextView videoTitleView;
        MaterialTextView videoDescriptionView;
        ConstraintLayout rootView;

        public VideoAdapterView(@NonNull View itemView) {
            super(itemView);

            thumbnailView = itemView.findViewById(R.id.video_thumbnail);
            videoTitleView = itemView.findViewById(R.id.video_title);
            videoDescriptionView = itemView.findViewById(R.id.video_description);
            rootView = itemView.findViewById(R.id.video_card_root);

        }
    }
}
