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
