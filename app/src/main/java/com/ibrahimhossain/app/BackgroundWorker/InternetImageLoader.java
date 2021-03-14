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

package com.ibrahimhossain.app.BackgroundWorker;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetImageLoader extends CustomAsyncTask<Void, Bitmap> {


    String url;
    ImageView imageView;
    Context context;
    int imageWidth;
    int onErrorSrc;
    int imageHeight;
    int placeHolder;

    ListenOnLoadResourceSetFailed listenOnLoadResourceSetFailed;

    Runnable runnable;

    Handler handler;


    @SuppressWarnings({"UnusedDeclaration"})
    public InternetImageLoader(String url, int placeholder, int onErrorSrc, ImageView imageView, Context context){

        this.url = url;
        this.imageView = imageView;
        this.context = context;
        this.onErrorSrc = onErrorSrc;
        this.placeHolder = placeholder;

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public InternetImageLoader(String url, int placeholder, int onErrorSrc, ImageView imageView, Context context, int imageWidth, int imageHeight){

        this.url = url;
        this.onErrorSrc = onErrorSrc;
        this.imageView = imageView;
        this.context = context;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.placeHolder = placeholder;

    }

    @Override
    protected void preExecute() {

        if(placeHolder > 0){

            try{

                runnable = () -> imageView.setImageResource(placeHolder);

             handler =   new Handler(Looper.getMainLooper());
             handler.post(runnable);
             handler.removeCallbacks(runnable);




            }catch (Exception ess){

                if(listenOnLoadResourceSetFailed != null){

                    listenOnLoadResourceSetFailed.reasonForFailer(ess);
                }


            }
        }
        super.preExecute();
    }

    @Override
    protected Bitmap doBackgroundTask() {


        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);




        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onTaskFinished(Bitmap bitmap) {

        if(bitmap != null){

            if(imageWidth >= 1 && imageHeight >= 1){

                handler = new Handler(Looper.getMainLooper());
                runnable = () -> {

                    try {
                        imageView.setImageBitmap(getResizeBitmap(bitmap, imageWidth, imageHeight));
                    }catch (Exception ess) {

                        if (listenOnLoadResourceSetFailed != null) {

                            listenOnLoadResourceSetFailed.reasonForFailer(ess);
                        }
                    }
                };

                handler.post(runnable);
                handler.removeCallbacks(runnable);







            }else{

                handler = new Handler(Looper.getMainLooper());
                runnable = () -> {
                    try{
                        imageView.setImageBitmap(bitmap);
                    }catch (Exception ec){

                        if(listenOnLoadResourceSetFailed != null){

                            listenOnLoadResourceSetFailed.reasonForFailer(ec);
                        }

                    }
                };

                handler.post(runnable);
                handler.removeCallbacks(runnable);

            }
        }else{

            handler = new Handler(Looper.getMainLooper());
            runnable = () -> {

                if(onErrorSrc > 0) {

                    try {
                        imageView.setImageResource(onErrorSrc);
                    }catch (Exception esss){

                        if(listenOnLoadResourceSetFailed != null){

                            listenOnLoadResourceSetFailed.reasonForFailer(esss);
                        }


                    }
                }


            };

            handler.post(runnable);
            handler.removeCallbacks(runnable);




        }

        super.onTaskFinished(bitmap);
    }



    public Bitmap getResizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public interface ListenOnLoadResourceSetFailed{


        void reasonForFailer(Exception exception);
    }

    public void setListenerOnResourceSetFailed(ListenOnLoadResourceSetFailed eventListener){

        this.listenOnLoadResourceSetFailed = eventListener;
    }

    @Override
    public void runThread() {
        if(imageView.getDrawable() == null){
            super.runThread();
        }

    }
}
