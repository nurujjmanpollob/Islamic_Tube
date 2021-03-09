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


    @SuppressWarnings({"UnusedDeclaration"})
    public InternetImageLoader(String url, int onErrorSrc, ImageView imageView, Context context){

        this.url = url;
        this.imageView = imageView;
        this.context = context;
        this.onErrorSrc = onErrorSrc;

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public InternetImageLoader(String url, int onErrorSrc, ImageView imageView, Context context, int imageWidth, int imageHeight){

        this.url = url;
        this.onErrorSrc = onErrorSrc;
        this.imageView = imageView;
        this.context = context;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

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

                new Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(getResizeBitmap(bitmap, imageWidth, imageHeight)));


            }else{

                new Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(bitmap));


            }
        }else{

            new Handler(Looper.getMainLooper()).post(() -> {
                if(onErrorSrc > 0) {
                    imageView.setImageResource(onErrorSrc);
                }
            });

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
}
