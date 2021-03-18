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

import android.os.Build;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class CustomAsyncTask<Progress, Result> {
    int numOfThreads = 1;
    static ExecutorService exc;




    public void runThread(){

        preExecute();

        if(numOfThreads > 0 && numOfThreads <= 8){

            exc = Executors.newFixedThreadPool(numOfThreads);
            exc.execute(() -> {

                Looper.prepare();
                onTaskFinished(doBackgroundTask());
                Looper.loop();

            });




        }

    }

    @MainThread
    protected void preExecute(){


    }

    @WorkerThread
    protected  abstract Result doBackgroundTask();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @MainThread
    protected void onTaskFinished(Result result) {

        exc.shutdown();

    }

    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    public void defineThreadCount(int numOfThread){
        this.numOfThreads = numOfThread;
    }

    @Deprecated
    @MainThread
    public static void cancalWork(){

        exc.shutdown();

    }


    @MainThread
    @SafeVarargs

    @SuppressWarnings({"UnusedDeclaration"})
    protected final void onProgressUpdated(Progress... progress){

    }


    @SafeVarargs
    private final void publishProgresssInternal(Progress... prg){

        onProgressUpdated(prg);
    }



    @SuppressWarnings({"UnusedDeclaration"})
    @SafeVarargs
    @WorkerThread
    protected final void publishProgress(Progress... values) {

       publishProgresssInternal(values);
    }


}

