package com.sana.android.plugin.camera;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.sana.android.plugin.camera.R;

public class ImagePreview extends Activity{

	private ImageView imgPreview;
	Uri fileuri,dataUri;
	String mimeType;
    byte[] bytes = null;
	ImageButton retake,keep;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.d("wel","intent");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		Log.d("set","set preview");
		try{
			
		final Intent extras=getIntent();
		fileuri = Uri.parse(extras.getStringExtra("uri"));
					
		imgPreview= (ImageView) findViewById(R.id.imageView1);
		imgPreview.setImageURI(fileuri);
		imgPreview.setRotation(90);
    			
		retake = (ImageButton) findViewById(R.id.button1);
    	keep = (ImageButton) findViewById(R.id.button2);
    	
    	retake.setOnClickListener(
    			new View.OnClickListener() {
    	            @Override
    	            public void onClick(View v) {
    	            	finish();
    	            }
    			}
    			);
    	
    	
    	keep.setOnClickListener(
    	        new View.OnClickListener() {
    	            @Override
    	            public void onClick(View v) {
    	            	    	            	  
    	            	try{
    	            	dataUri=Uri.parse(extras.getStringExtra("datauri"));
    	            	mimeType=extras.getStringExtra("mime");
    	            	bytes = convertImageToByte(fileuri) ;
    	    		    OutputStream os = null;
    	    		    try{
    	    		        os = getContentResolver().openOutputStream(dataUri);
    	    		        os.write(bytes);
    	    		    } catch(Exception e){
    	    		    }  
    	    		    finally {
    	    		        if(os != null)
								try {
									os.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
    	    		    }
    	    		    Intent result = new Intent();
    	    		    result.setDataAndType(dataUri,dataUri.toString() );
    	    		    setResult(RESULT_OK,result);
    	    		    
    	    		    
    	    		    }catch(Exception e)
    	    		    {
    	    		    	e.printStackTrace();
    	    		    }
    	            	
    	            	deleteFiles();
    	            	finish();
    	            	
    	            }
    	            }
    	            );
    	
    	
		}catch(Exception e)
		{
			Log.d("hello","exception");
		}

	}
	
private void deleteFiles() {
	 //delete temporary file

    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
              Environment.MEDIA_UNKNOWN),"sana");
    if (mediaStorageDir.isDirectory()) {
        String[] children = mediaStorageDir.list();
        for (int i = 0; i < children.length; i++) {
            new File(mediaStorageDir, children[i]).delete();
        }
    }
	}


	@Override
	public void onBackPressed() {
	    // do something on back.
	    return;
	}
	
	public byte[] convertImageToByte(Uri uri){
		byte[] data=null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}