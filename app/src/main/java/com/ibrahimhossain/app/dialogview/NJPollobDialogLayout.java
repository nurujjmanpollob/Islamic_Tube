package com.ibrahimhossain.app.dialogview;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;


import com.google.android.material.textview.MaterialTextView;
import com.ibrahimhossain.app.R;


import pl.droidsonroids.gif.GifImageView;


public class NJPollobDialogLayout extends Dialog
{
	Context context;
	GifImageView ImgSrcView;
	AppCompatButton yesButton;
	AppCompatButton noButton;
	MaterialTextView descriptionView;
	String thumbnailURL;
	String description;
	int thumbnailResourceID = 0;
	String leftButtonText;
	String rightButtonText;
	
	
	public NJPollobDialogLayout(Context context){
		super(context);
		this.context = context;
	}




	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//set the content view
		setContentView(R.layout.njpollob_dialog_layout);




		ImgSrcView = findViewById(R.id.dialog_imageview);
		yesButton = findViewById(R.id.dialog_btn_yes);
	    noButton = findViewById(R.id.dialog_btn_no);
		descriptionView = findViewById(R.id.dialog_txt);
		
		if(thumbnailResourceID > 0){
			
			ImgSrcView.setImageResource(thumbnailResourceID);
		}
		
		if(description != null){
			
			descriptionView.setText(description);
			
		}
		
		yesButton.setOnClickListener(this::callOnLeftClick);
		

		noButton.setOnClickListener(this::callOnRightClick);
			
			if(leftButtonText != null){
				
				yesButton.setText(leftButtonText);
			}else {

				yesButton.setVisibility(View.GONE);

			}
			
			if(rightButtonText != null){
				
				noButton.setText(rightButtonText);
			}else {

				noButton.setVisibility(View.GONE);
			}



		
		
	}
	
	public void setThumbnailByURL(String url, Boolean isUseCacheStrategy){
	
	//lets run background worker 😂
		
	NJPollobDialogWorker worker = new NJPollobDialogWorker(context, isUseCacheStrategy, url);

		worker.setEventListenerForTask(new NJPollobDialogWorker.ListenOnResourceLoadEvent(){

				@Override
				public void onLoadingResource()
				{
					ImgSrcView.setImageResource(R.drawable.loading_animation);
				}

				@Override
				public void onSuccessfullyExecution(String cacheDir, String fileName)
				{
				
					ImgSrcView.setImageURI(new CacheUriPerser(cacheDir, fileName).ReturnWorkingUri());
				}

				@Override
				public void onReceivedError(Exception exceptionToRead)
				{
				

				}
				
		
		
	});


		worker.runThread();
		
	}
	
	public void setThumbnailByResource(int resourceID){
		
		ImgSrcView.setImageResource(resourceID);
		this.thumbnailResourceID = resourceID;
		
	}
	
	public void setDialogDescription(String details){
		
		this.description = details;
		
	}
	
	//set up interface and listen on button click listener
	public interface DialogButtonClickListener{
		
		void onLeftButtonClick(View view);
		void onRightButtonClick(View view);
		
	}
	
	private DialogButtonClickListener buttonClickListener = null;
	
	public void setListenerOnDialogButtonClick(String leftButtonText, String rightButtonText, DialogButtonClickListener baseClassInnerInterfacePassHere){
		
		this.buttonClickListener = baseClassInnerInterfacePassHere;

		if(leftButtonText != null) {
			this.leftButtonText = leftButtonText;
		}
		if (rightButtonText != null) {
			this.rightButtonText = rightButtonText;
		}
		
	}
	
	private void callOnRightClick(View vw){
		this.buttonClickListener.onRightButtonClick(vw);
	}
	
	private void callOnLeftClick(View vw){
		
		this.buttonClickListener.onLeftButtonClick(vw);
	}
	
}
