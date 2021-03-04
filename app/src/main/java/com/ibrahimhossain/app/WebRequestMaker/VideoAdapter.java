package com.ibrahimhossain.app.WebRequestMaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.ibrahimhossain.app.R;
import com.ibrahimhossain.app.VideoData;
import com.ibrahimhossain.app.dialogview.CacheUriPerser;
import com.ibrahimhossain.app.dialogview.NJPollobDialogWorker;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterView holder, int position) {


        //Get thumbnail url and set Image thumbnail
        NJPollobDialogWorker worker = new NJPollobDialogWorker(applicationContext, false, videoData.get(position).getThumbnailURL());

        worker.runThread();

        worker.setEventListenerForTask(new NJPollobDialogWorker.ListenOnResourceLoadEvent() {
            @Override
            public void onLoadingResource() {



            }

            @Override
            public void onSuccessfullyExecution(String cacheDir, String fileName) {

                holder.thumbnailView.setImageURI(new CacheUriPerser(cacheDir, fileName).ReturnWorkingUri());

            }

            @Override
            public void onReceivedError(Exception exceptionToRead) {

            }
        });



        //set title
        holder.videoTitleView.setText(videoData.get(position).getTitle());

        //Set description
        holder.videoDescriptionView.setText(videoData.get(position).getDescription());

        //Get video url

        String videoURL = videoData.get(position).getVideoURL();

        //Lets create some animation

        Animation animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in);

        //Set animation to root view
        holder.rootView.setAnimation(animation);






    }

    @Override
    public int getItemCount() {
        return videoData.size();
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
