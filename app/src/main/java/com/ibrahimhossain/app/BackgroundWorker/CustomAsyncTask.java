package com.ibrahimhossain.app.BackgroundWorker;

import android.os.Build;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public abstract class CustomAsyncTask<Progress, Result>
{
    int numOfThreads = 1;



    static ExecutorService exc;



    static Looper looper;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @MainThread

    public void runThread(){


        looper = Looper.myLooper();

        preExecute();
     //   System.out.println("Starting, "+numOfThreads+" Thread Combination in one task");

        if(numOfThreads > 0 && numOfThreads <= 8){

            exc = Executors.newFixedThreadPool(numOfThreads);
            exc.execute(() -> {

                Looper.prepare();
                Result result= doBackgroundTask();

                onTaskFinished(result);



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
    protected void onTaskFinished(Result result) {

        exc.shutdown();

    }

    @MainThread
    public void defineThreadCount(int numOfThread){

        this.numOfThreads = numOfThread;
    }

    @MainThread
    public static void cancalWork(){

        exc.shutdown();

    }


    @SafeVarargs
    @MainThread
    protected final void onProgressUpdated(Progress... progress){

    }


    @SafeVarargs
    @WorkerThread
    protected final void publishProgress(Progress... values) {

        onProgressUpdated(values);
    }


}
