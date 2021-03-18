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
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.circularreveal.CircularRevealRelativeLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibrahimhossain.app.BackgroundWorker.WebRequestMaker;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteVideoAdapter extends RecyclerView.Adapter<FavoriteVideoAdapter.FavoriteVideoAdapterView> {


    private Context context;
    private List<FavoriteVideoDatabase> databases;
    private View errorView;

    public FavoriteVideoAdapter(Context context, List<FavoriteVideoDatabase> favVidData, View errorView){


        this.context = context;
        this.databases = favVidData;
        this.errorView = errorView;
    }


    @NonNull
    @Override
    public FavoriteVideoAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_video_list_row, parent, false);

        return new FavoriteVideoAdapterView(layoutView);


    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVideoAdapterView holder, int position) {

        holder.videoTitleView.setText(databases.get(position).getTitle());
        holder.videoDescriptionView.setText(databases.get(position).getDescription());

        holder.rootView.setOnLongClickListener(v -> {

            deleteFavoriteVideoFromList(databases.get(position).getTitle(), databases.get(position).getDescription());
            return true;
        });


        holder.rootView.setOnClickListener(v -> {

            WebRequestMaker requestMaker = new WebRequestMaker(databases.get(position).getDescription());
            requestMaker.setEventListener(new WebRequestMaker.WebRequestEvent() {

                KProgressHUD kProgressHUD;

                @Override
                public void onStartExecuting() {

                    kProgressHUD = new KProgressHUD(context);
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


                    Intent i = new Intent(context, VideoDetails.class);
                    i.putExtra(Variables.VIDEO_DETAILS_INTENT_KEY, result);
                    i.putExtra(Variables.VIDEO_DETAILS_URL_RAW_KEY, databases.get(position).getDescription());
                    context.startActivity(i);


                }

                @Override
                public void onLoadFailed(String cause) {




                    if(kProgressHUD.isShowing()){
                        kProgressHUD.dismiss();
                    }


                    NJPollobDialogLayout layout = new NJPollobDialogLayout(context);
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

        if(databases.size() != 0){

            return databases.size();
        }else {
            return 0;
        }

    }

    static class FavoriteVideoAdapterView extends RecyclerView.ViewHolder {


        MaterialTextView videoTitleView;
        MaterialTextView videoDescriptionView;
        CircularRevealRelativeLayout rootView;


        public FavoriteVideoAdapterView(@NonNull View itemView) {
            super(itemView);


            rootView = itemView.findViewById(R.id.fav_video_list_root);
            videoTitleView = itemView.findViewById(R.id.fav_video_list_title);
            videoDescriptionView = itemView.findViewById(R.id.fav_video_list_description);

        }
    }




    private void deleteFavoriteVideoFromList(final String title, final String link) {



        new AlertDialog.Builder(context)
                .setTitle("DELETE")
                .setMessage("Confirm that you want to delete this bookmark?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Variables.FAVORITE_VIDEO_DATASHEET_NAME, Context.MODE_PRIVATE);
                    String jsonLink = sharedPreferences.getString(Variables.FAVORITE_VIDEO_URL, null);
                    String jsonTitle = sharedPreferences.getString(Variables.FAVORITE_VIDEO_TITLE, null);


                    if (jsonLink != null && jsonTitle != null) {


                        Gson gson = new Gson();
                        ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                        }.getType());

                        ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                        }.getType());


                        linkArray.remove(link);
                        titleArray.remove(title);


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Variables.FAVORITE_VIDEO_URL, new Gson().toJson(linkArray));
                        editor.putString(Variables.FAVORITE_VIDEO_TITLE, new Gson().toJson(titleArray));
                        editor.apply();

                       refreshDatabase();


                    }
                    dialogInterface.dismiss();
                })

                .setNeutralButton("Delete All", (p1, p2) -> {

                    SharedPreferences   sharedPreferences = context.getSharedPreferences(Variables.FAVORITE_VIDEO_DATASHEET_NAME, Context.MODE_PRIVATE);
                    String jsonLink = sharedPreferences.getString(Variables.FAVORITE_VIDEO_URL, null);
                    String jsonTitle = sharedPreferences.getString(Variables.FAVORITE_VIDEO_TITLE, null);


                    if (jsonLink != null && jsonTitle != null) {


                        Gson gson = new Gson();
                        ArrayList<String> linkArray = gson.fromJson(jsonLink, new TypeToken<ArrayList<String>>() {
                        }.getType());

                        ArrayList<String> titleArray = gson.fromJson(jsonTitle, new TypeToken<ArrayList<String>>() {
                        }.getType());


                        linkArray.clear();
                        titleArray.clear();


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Variables.FAVORITE_VIDEO_URL, new Gson().toJson(linkArray));
                        editor.putString(Variables.FAVORITE_VIDEO_TITLE, new Gson().toJson(titleArray));
                        editor.apply();



                      refreshDatabase();

                    }



                })



                .setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss()).show();


    }



    public void filterList(List<FavoriteVideoDatabase> dataList){

        databases = dataList;

        new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);


    }


    private void refreshDatabase(){

        FavVideoDataReader reader = new FavVideoDataReader(context);
        reader.setEvent(new FavVideoDataReader.OnFavVideoDataLoadEvent() {
            @Override
            public void onHasDatabase(List<FavoriteVideoDatabase> databaseList) {

                if(databaseList != null){

                    filterList(databaseList);
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
    }


}
