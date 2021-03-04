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
