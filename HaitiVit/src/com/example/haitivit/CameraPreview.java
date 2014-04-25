package com.example.haitivit;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CameraPreview extends MainActivity{

	private ImageView imgPreview;
	Uri fileuri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		
		imgPreview= (ImageView) findViewById(R.id.imageView1);
		Intent intent = getIntent();
		fileuri=intent.getData();
		imgPreview.setImageURI(fileuri);

	}
}
