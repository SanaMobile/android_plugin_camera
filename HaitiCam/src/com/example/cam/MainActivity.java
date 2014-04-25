package com.example.cam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.cam.CameraPreview;
import com.example.cam.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class MainActivity extends Activity {

	protected static final String TAG = null;
	protected static final Throwable e = null;
	private Camera mCamera;
	private CameraPreview mPreview;
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String flash = getIntent().getStringExtra("concept");
        
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.frameLayout1);
        preview.addView(mPreview);
        
        
        
        Camera.Parameters params = mCamera.getParameters();
        // set the focus mode
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        // set Camera parameters
        mCamera.setParameters(params);
        
        
     // Add a listener to the Capture button
	    Button captureButton = (Button) findViewById(R.id.buttonCapture);
	    captureButton.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // get an image from the camera
	                mCamera.takePicture(null, null, mPicture);
	                
	            }
	        }
	    );
	    
	    		
	}
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {

	        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	        if (pictureFile == null){
	            Log.d(TAG, "Error creating media file, check storage permissions: " +
	                e.getMessage());
	            return;
	        }
	        
	        Uri fiuri= Uri.fromFile(pictureFile);
	        Intent result=new Intent();
			result.setData(fiuri);
			setResult(RESULT_OK,result);
			
	        mPreview.stopPreviewAndFreeCamera();
	        finish();

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	    }
	};
	

	
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Sana");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    }  else {
	        return null;
	    }

	    return mediaFile;
	}
}
