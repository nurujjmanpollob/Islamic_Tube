package com.ibrahimhossain.app.BackgroundWorker;

import android.os.Handler;

import java.io.IOException;

public class ThreadFixer
{


    Handler handler;

    public ThreadFixer(Handler handler){

        this.handler = handler;


        handler.post(() -> {
            if(fixer != null){

                try {
                    fixer.onSuccessFullFix();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




    }



    public interface WrongThreadFixer{

        void onSuccessFullFix() throws IOException;

    }

    public WrongThreadFixer fixer = null;

    public void setListenerForFixThread(WrongThreadFixer wrongThreadFixer){

        this.fixer = wrongThreadFixer;

    }
}
