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


package com.ibrahimhossain.app.dialogview;


import android.annotation.SuppressLint;

import android.content.*;


import com.ibrahimhossain.app.BackgroundWorker.CustomAsyncTask;

import java.io.*;
import java.net.*;

public class NJPollobDialogWorker extends CustomAsyncTask<Void, String>
{





	@SuppressLint("StaticFieldLeak")
	Context contextWrapper;
	Boolean isUseCache;
	String inputURL;
	String filenameWithExtension = "njpollob";
	
	public NJPollobDialogWorker(Context context, Boolean flagForCache, String urlToProcess){
		this.contextWrapper = context;
		this.isUseCache = flagForCache;
		this.inputURL = urlToProcess;
		
	}


	@Override
	protected void preExecute() {

		callOnLoadStart();
		super.preExecute();
	}

	private String operateNetWorkOperation(String cacheDirectory){
		
		/*Perform network operation and get file and save in cache storage space */
		
		try
		{

			File f = new File(cacheDirectory, filenameWithExtension);

			FileOutputStream output = new FileOutputStream(f);
			URL url = new URL(inputURL);
			HttpURLConnection connction = (HttpURLConnection) url.openConnection();
			InputStream InputStream = connction.getInputStream();
			
			byte[] buffer = new byte[1024];
			int lenght;
			
			while((lenght = InputStream.read(buffer)) > 0){
				
				output.write(buffer, 0, lenght);
			}
			
			output.close();
			return cacheDirectory;
		}
		catch (Exception e)
		{
		
			callOnReceivedException(e);
			return null;
		}

	}

	@Override
	protected void onTaskFinished(String result) {

		//if result is successful
		if(result != null){

			callOnLoadSuccessful(result, filenameWithExtension);

		}

		super.onTaskFinished(result);
	}





	@Override
	protected String doBackgroundTask() {
		String cacheDIR = contextWrapper.getCacheDir().getPath()+"/";

		//check to see if use cache method
		if(isUseCache){
			//Let's check this file is exists or not in cache directory
			File file = new File(cacheDIR, filenameWithExtension);

			if(file.exists()){

				//file is found
				//get file lenght
				long getFileSizeInBytes = file.length();
				//let's check this is not - null;
				if(getFileSizeInBytes > 1024){

					return cacheDIR;
				}

				if(getFileSizeInBytes < 1024){
					//file is null, let's run network operation
					filenameWithExtension = filenameWithExtension+"1";
					return	operateNetWorkOperation(cacheDIR);

				}

			}
			//file is not found!
			else{

				filenameWithExtension = filenameWithExtension+"1";

				return operateNetWorkOperation(cacheDIR);
			}

		}

		if(!isUseCache){
			//we are now running network opeation

			File file = new File(cacheDIR, filenameWithExtension);
			if(file.exists()){

				filenameWithExtension = filenameWithExtension+"1";
			}
			return	operateNetWorkOperation(cacheDIR);
		}

		return null;
	}

	public interface ListenOnResourceLoadEvent{
		
		void onLoadingResource();
		void onSuccessfullyExecution(String cacheDir, String fileName);
		void onReceivedError(Exception exceptionToRead);
		
	}
	
	//* Creating interface and give callback for various events*/
	
	private ListenOnResourceLoadEvent eventListener = null;
	
	public void setEventListenerForTask(ListenOnResourceLoadEvent insertEventListenerInterface){
		
		this.eventListener = insertEventListenerInterface;
	}
	
	private void callOnLoadStart(){
		
		if(eventListener != null){
			
			this.eventListener.onLoadingResource();
		}
		
		}
		private void callOnLoadSuccessful(String dir1, String dir2){
			
			if(eventListener != null){
				
				this.eventListener.onSuccessfullyExecution(dir1, dir2);


				
			}
			
		}
		
		private void callOnReceivedException(Exception ess){
			
			if(eventListener != null){
				
				this.eventListener.onReceivedError(ess);
			}
		}

		public void deleteCache(){

			if(!isUseCache){

				File f = new File(contextWrapper.getCacheDir().getPath()+"/", filenameWithExtension);
				if(f.exists()){

					f.delete();
				}
			}
		}
	
	
}
