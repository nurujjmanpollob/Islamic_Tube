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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class NJPollobLogWriter
{
    String dir;
    String fileName;
    String exception;
    String className;
    boolean isUseAppendLog = true;

    public NJPollobLogWriter(String directoryInStringFromat, String fileNamewithExtension, String exceptionDetails){

        this.dir = directoryInStringFromat;
        this.fileName = fileNamewithExtension;
        this.exception = exceptionDetails;

    }

    public String getLogsFromFile(){

        File file = new File(dir+"\n", fileName);
        StringBuilder sb = new StringBuilder();
        if(file.exists()){

            try
            {
                FileReader reader = new FileReader(file);
                BufferedReader bfreader = new BufferedReader(reader);
                String line;

                while((line=bfreader.readLine()) != null){

                    sb.append(line);
                    sb.append("\n");
                }

                bfreader.close();
                return sb.toString();
            }
            catch (Exception e)
            {

                callMeOnGeneralError(e);
            }
        }

        return null;
    }

    public void performWriteOperation(){

        //Start making file by dirs and file name

        File file = new File(dir+"/", fileName);

        if(isUseAppendLog){

            if(file.exists()){

                operateAppendOperation(file);
            }else{

                operateFirstWrite(file);
            }


        }
        if(!isUseAppendLog){

            operateFirstWrite(file);
        }


    }


    public void setClassNameForLog(String className){

        this.className = className;

    }

    public void isIncludeLogToExistingLogFile(Boolean flag){

        this.isUseAppendLog = flag;

    }

    private void operateAppendOperation(File file){

        String currentTimeData = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        //Start try
        try{

            FileReader reader = new FileReader(file);
            BufferedReader bfreader = new BufferedReader(reader);
            String line;
            StringBuilder textInside = new StringBuilder();

            //start while loop
            while((line= bfreader.readLine()) != null){

                textInside.append(line);
                textInside.append("\n");

            }

            textInside.append("[BEGIN INCLUDE]\n\n");
            if(className != null){
                textInside.append("Class Name:  ").append(className).append("\n");
            }
            textInside.append("Time when recorded:  "+currentTimeData+"\n\n"+"Message:  "+exception+"\n\n"+"[END INCLUDE]\n\n");

            //close reader
            bfreader.close();

            // now we need to write back all exception!
            FileWriter w = new FileWriter(file);
            w.append(textInside);
            w.flush();
            w.close();
        }catch(Exception ess){

            callMeOnGeneralError(ess);

        } // end try

    }

    private void operateFirstWrite(File file){

        try
        {
            String currentTimeData = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            FileWriter w = new FileWriter(file);
            w.append("[BEGIN INCLUDE]\n\n");
            if(className != null){
                w.append("Class Name:  ").append(className).append("\n");
            }
            w.append("Time when recorded:  ").append(currentTimeData).append("\n\n").append("Message:  ").append(exception).append("\n\n").append("[END INCLUDE]\n\n");
            w.flush();
            w.close();
        }
        catch (IOException jje)
        {

            callMeOnIOError(jje);
        }

    }

    //Set up listener interface

    public interface OnSomethingWrong{

        public void onIOError(IOException IOException);
        public void onGeneralException(Exception Exception);
    }

    private OnSomethingWrong errCls = null;

    private void callMeOnIOError(IOException ioerr){
        if(errCls != null){
            this.errCls.onIOError(ioerr);
        }
    }
    private void callMeOnGeneralError(Exception exc){
        if(errCls != null){
            this.errCls.onGeneralException(exc);
        }
    }

    public void setListnerForError(OnSomethingWrong NJPollobExceptionWriterDotOnSomethingWrong){

        this.errCls = NJPollobExceptionWriterDotOnSomethingWrong;
    }

}

