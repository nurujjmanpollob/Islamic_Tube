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

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.ibrahimhossain.app.BackgroundWorker.HTMLThemeColorGetter;
import com.ibrahimhossain.app.BackgroundWorker.ThreadFixer;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;

public class WebReferenceLoader extends AppCompatActivity {

    WebView loader;
    String URL = "";
    private KProgressHUD hud;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        setContentView(R.layout.web_reference_layout);
        loader = findViewById(R.id.web_reference_main_webview);


        //Set up method to check if there are any data available
        checkIntentData(getIntent());

        //A little bit of work to check if an URL is valid or not!
        //note, we are using try / catch because sometimes we might be get ThreadUncaughtException
        try{

            //Check if url is valid
            if(URLUtil.isValidUrl(URL)){

                loader.loadUrl(URL);
            }

            //the url is invalid maybe, so lets make a simple google search!
            else{

                loader.loadUrl(Variables.SEARCH_ENGINE_URL_GOOGLE+URL);

            }

        }
        //Something unexpacted happened, lets make a toast and write log in system

        catch(Exception e){

            Toast.makeText(WebReferenceLoader.this, e.toString(), Toast.LENGTH_LONG).show();

        }

        //lets set WebViewClient listener stuff
        loader.setWebViewClient(new WebViewClient(){

            //CallBack when an url and resource is under loading
            @Override
            public void onLoadResource(WebView view, String url){

                //TODO: do whatever you like here!

            }

        });

        //set WebChromeClient Listener
        loader.setWebChromeClient(new WebChromeClient(){


            //get parcentage of current loading
            @Override
            public void onProgressChanged(WebView view, int newProgress){



                hud = KProgressHUD.create(WebReferenceLoader.this);
                hud.setStyle(KProgressHUD.Style.BAR_DETERMINATE);
                hud.setLabel("Please wait");

                hud.setMaxProgress(100);
                hud.setProgress(newProgress);

                if(newProgress >= 80){

                    hud.setLabel("Almost Loaded");
                }

                if(newProgress == 100){

                    if(hud.isShowing()){

                        hud.dismiss();
                    }
                }


                super.onProgressChanged(view,newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {

                HTMLThemeColorGetter colorGetter = new HTMLThemeColorGetter(view.getUrl());
                colorGetter.ListenOnLoadEvent(new HTMLThemeColorGetter.ListenOnGetEvent() {
                    @Override
                    public void onSuccessfullColorExtraction(String colorStr) {


                        ThreadFixer fixer = new ThreadFixer(new Handler(Looper.getMainLooper()));
                        fixer.setListenerForFixThread(() -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colorStr != null) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(Color.parseColor(colorStr));
                                window.setNavigationBarColor(Color.parseColor(colorStr));
                            }
                        });




                    }

                    @Override
                    public void onExceptionOccured(String exceptionMsg) {

                        ThreadFixer fixer = new ThreadFixer(new Handler(Looper.getMainLooper()));
                        fixer.setListenerForFixThread(() -> {

                            Palette palette = Palette.from(icon).generate();
                            int colorLight = palette.getLightVibrantColor(0x000000);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(colorLight);
                                window.setNavigationBarColor(colorLight);
                            }

                        });




                    }
                });


                super.onReceivedIcon(view, icon);
            }
        });

        //set up downloader
        loader.setDownloadListener(new DownloadListener(){

            long id;

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size)
            {

                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/", "NJP Entertainment Downloads");
                if(file.exists()){

                    file.mkdir();

                }

                DownloadManager.Request downloader = new DownloadManager.Request(Uri.parse(url));
                downloader.allowScanningByMediaScanner();
                downloader.setAllowedOverMetered(true);
                downloader.setAllowedOverRoaming(true);
                downloader.setDescription("File downloading from: "+url);
                downloader.setTitle("NJP Entertainment downloading file");
                downloader.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getPath(), "NJP Entertainment Downloads");
                downloader.setShowRunningNotification(true);
                downloader.setVisibleInDownloadsUi(true);

                //lets queque current download
                DownloadManager manager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                }
                assert manager != null;
                id =manager.enqueue(downloader);

                //Let's query download manager
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor =	manager.query(query);
                cursor.moveToFirst();
                //Let's check how many of bytes downloaded!
                @SuppressWarnings({"UnusedDeclaration"})
                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                cursor.close();




                //I will add a alart Dialog with progressbar saying parcentage downloaded


            }

            @SuppressWarnings({"UnusedDeclaration"})

            BroadcastReceiver receiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent in)
                {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    String action = in.getAction();
                    if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){

                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(id);

                        // Check if file has been successfully downloaded
                        Cursor c = downloadManager.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            int status = c.getInt(columnIndex);
                            // If everything is fine, call the abstract method and proceed
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                Uri uri = Uri.parse(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                                onDownloadDone(uri);
                                // Otherwise tell the user that the download failed
                            }
                            context.unregisterReceiver(this);

                        }
                        c.close();
                    }

                }



            };

            //



        });

        //Lets set up BroadCastReceiver for this activity




        super.onCreate(savedInstanceState);
    }

    // Not a bad idea! here I will check for all intent data that sent with different key
    private void checkIntentData(Intent intent)
    {


        //check if the intent has url!
        if(intent.hasExtra(Variables.WEB_REFERENCE_INTENT_KEY)){

            URL = intent.getStringExtra(Variables.WEB_REFERENCE_INTENT_KEY);
        }

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        //check intent data
        checkIntentData(intent);

        super.onNewIntent(intent);
    }



    @Override
    public void onBackPressed()
    {
        // TODO: Implement this method
        super.onBackPressed();
    }

    @Override
    protected void onPause()
    {
        // TODO: Implement this method
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        // TODO: Implement this method
        super.onResume();
    }


    private void onDownloadDone(Uri uri)
    {
        //Let's check mime type, and open though Intent
        String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }

        //we have done get file extension and mime, lets fire an intent!

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        startActivity(intent);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (loader != null) {
                if (loader.canGoBack()) {

                    loader.goBack();
                } else {

                    WebReferenceLoader.this.finish();

                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
