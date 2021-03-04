package com.ibrahimhossain.app.BackgroundWorker;


import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundExecutor implements Thread.UncaughtExceptionHandler
{

    @Override
    public void uncaughtException(@NonNull Thread p1, @NonNull Throwable p2)
    {
        if(task != null){

            task.onException("The Thread: "+p1.getName()+" is dead, Fatal Exception occured");
            Log.d("BackgroundExecutor:", "The Thread: "+p1.getName()+" is dead, Fatal Exception occured");
        }
    }


    int threadsToRun = 1;

    ExecutorService service;
    boolean taskFinished = false;
    int count = 0;


    public BackgroundExecutor(int numOfThread){

        this.threadsToRun = numOfThread;

    }

    public void requestDownThread(Object object){


        taskFinished = true;

        service.shutdown();
        if(task != null){
            task.onTaskFinish();

        }




    }


    public void runThread(){


        if(threadsToRun > 0 && threadsToRun <= 8){
            service = Executors.newFixedThreadPool(threadsToRun);



            if( task != null){

                task.onPreExecution();

                service.submit(() -> {



                    if(task != null && count == 0){
                        task.onRunningTask();
                    }


                    assert task != null;
                    finishTask(task.onRunningTask());


                });


                count++;

            }


            if(!taskFinished){

                service.shutdown();
            }

        }else{
            if(task != null){
                task.onException("The thread count is must between 1-8, it shouldn't be lower or exceed then that!");
            }}



    }



    public interface TaskStatus{
        void onPreExecution();
        boolean onRunningTask();
        void onException(String errorMsg);
        void onTaskFinish();
    }

    private TaskStatus task = null;

    public void listenOnBackGroundTask(TaskStatus taskListener){
        this.task = taskListener;

    }

    private void finishTask(boolean param){

        if(param && task != null){


            task.onTaskFinish();
            service.shutdown();
        }

        if(!param){

            service.shutdown();
        }
    }

}
