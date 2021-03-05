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


import android.net.*;
import java.io.*;

import android.widget.*;

import pl.droidsonroids.gif.GifImageView;

public class CacheUriPerser
{
	Uri uri;
	String cacheDR;
	String file;
	GifImageView gifImg;
	ImageView imgviw;
	
	
	public CacheUriPerser(String cacheDir, String FileName){

		this.cacheDR = cacheDir;
		this.file = FileName;
		
		
	}
	
	public CacheUriPerser(GifImageView gifImageViewInstance, String cacheDir, String fileName){
		this.gifImg = gifImageViewInstance;
		this.cacheDR = cacheDir;
		this.file = fileName;
	}
	
	public CacheUriPerser(ImageView imageView, String cacheDir, String fileName){
		
		this.cacheDR = cacheDir;
		this.file = fileName;
		this.imgviw = imageView;
		
	}
	
	public void setUpResources(){
		
		if(imgviw != null){
		
			imgviw.setImageURI(Uri.fromFile(new File(cacheDR, file)));
			
		}
		
		if(gifImg != null){
			
			gifImg.setImageURI(Uri.fromFile(new File(cacheDR, file)));
		}
	}
	
	
	public Uri ReturnWorkingUri(){
		
		return uri = Uri.fromFile(new File(cacheDR, file));
	}
}
