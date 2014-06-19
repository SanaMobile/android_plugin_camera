package com.example.cam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.cam.CameraPreview;
import com.example.cam.R;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ZoomControls;


public class MainActivity extends Activity {

	protected static final String TAG = null;
	protected static final String TAG1 = "hello";
	protected static final Throwable e = null;
	private Camera mCamera;
	private CameraPreview mPreview;
	public static final int MEDIA_TYPE_IMAGE = 1;
    Camera.Parameters params;
    int currentZoomLevel = 1, maxZoomLevel = 0;
    String flash,shutter,qua,white;
    int quality;
	
    
	@SuppressLint("CutPasteId")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.frameLayout1);
        preview.addView(mPreview);
        
        params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        
        mCamera.enableShutterSound(true);
        
        params.setJpegQuality(100);
        mCamera.setParameters(params);
        
       
        //params.setPictureSize(800, 480);
        //mCamera.setParameters(params);
       
        Log.i(MainActivity.class.getSimpleName(),"Intent caught:"+getIntent());
       try{
    	   
    	   flash = getIntent().getStringExtra("flash");
    	   white = getIntent().getStringExtra("white");
    	   shutter = getIntent().getStringExtra("shutter");
    	   qua = getIntent().getStringExtra("quality");
    	   quality=Integer.parseInt(qua);
    	   
    	   
    	   //flash mode
        	if(!TextUtils.isEmpty(flash) && flash.equals("yes"))
        	{
                Log.d(TAG,"flash"+flash);

            params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(params);
        	}
        	else
        	{
        		params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(params);
        	}
        	
        	
        	//white balance
        	if(!TextUtils.isEmpty(white) && white.equals("yes"))
        	{
        		Log.d(TAG,"white"+white);
                params = mCamera.getParameters();
                params.setAutoWhiteBalanceLock(true);
                mCamera.setParameters(params);

            	}
            	else
            	{
            		params = mCamera.getParameters();
            		params.setAutoWhiteBalanceLock(false);
                    mCamera.setParameters(params);

            	}
        	
        	//jpeg quality
        	if(!TextUtils.isEmpty(qua))
        	{
        	Log.d(TAG,"qua"+quality);
        	params = mCamera.getParameters();
        	params.setJpegQuality(quality);
            mCamera.setParameters(params);

        	}
        	else
        	{
        		params = mCamera.getParameters();
        		params.setJpegQuality(100);
                mCamera.setParameters(params);

        	}
        	}
        catch(Exception e)
        {
        	Log.e(TAG1, "Error getting intent " + e.toString());
        }
        
        
        
        
     // Add a listener to the Capture button
        ZoomControls zoomControls = (ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);
        
	    ImageButton captureButton = (ImageButton) findViewById(R.id.imageButton1);
	    preview.removeView(captureButton);
	    preview.addView(captureButton); 
	    preview.removeView(zoomControls);
	    preview.addView(zoomControls);
	    
	    captureButton.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // get an image from the camera
	            	
	            	if(!TextUtils.isEmpty(shutter) && shutter.equals("yes"))
	                mCamera.takePicture(shutterCallback, null, mPicture);
	            	
	            	else
	            		mCamera.takePicture(null, null, mPicture);
	            }
	            private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
	                public void onShutter() {
	                    AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	                    mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
	                }
	            };
	        }
	    );
	    
	    
	    params=mCamera.getParameters();

        ZoomControls zoomControls1 = (ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);
        if(params.isZoomSupported() && params.isSmoothZoomSupported()){    
        maxZoomLevel = params.getMaxZoom();
                
        Log.d(TAG, "zoomvalue " + maxZoomLevel);
        Boolean s=params.isSmoothZoomSupported();
        Log.d(TAG, "s"+s);
        
        zoomControls1.setIsZoomInEnabled(true);
        zoomControls1.setIsZoomOutEnabled(true);
        
        zoomControls1.setOnZoomInClickListener(new OnClickListener(){
                public void onClick(View v){
                        if(currentZoomLevel < maxZoomLevel){
                            currentZoomLevel=currentZoomLevel+10;
                        	mCamera.startSmoothZoom(currentZoomLevel);
                        	Log.d(TAG, "zoom2 " + currentZoomLevel);
                            //params.setZoom(currentZoomLevel);
                            //mCamera.setParameters(params);

                        }
                }
            });

        zoomControls1.setOnZoomOutClickListener(new OnClickListener(){
                public void onClick(View v){
                        if(currentZoomLevel > 0){
                            currentZoomLevel=currentZoomLevel-5;
                            if(currentZoomLevel<0)
                            	currentZoomLevel=0;
                        	mCamera.startSmoothZoom(currentZoomLevel);
                        	Log.d(TAG,"zoom3");
                            //params.setZoom(currentZoomLevel);
                            //mCamera.setParameters(params);

                        }
                }
            }); 
        
       }else if (params.isZoomSupported() && !params.isSmoothZoomSupported()){
           //stupid HTC phones
           maxZoomLevel = params.getMaxZoom();

           zoomControls.setIsZoomInEnabled(true);
           zoomControls.setIsZoomOutEnabled(true);

           zoomControls.setOnZoomInClickListener(new OnClickListener() {
               public void onClick(View v) {
                   if (currentZoomLevel < maxZoomLevel) {
                       currentZoomLevel++;
                       Log.d(TAG,"zoom4");
                   	   params.setZoom(currentZoomLevel);
                       mCamera.setParameters(params);

                   }
               }
           });

           zoomControls.setOnZoomOutClickListener(new OnClickListener() {
               public void onClick(View v) {
                   if (currentZoomLevel > 0) {
                       currentZoomLevel--;
                       params.setZoom(currentZoomLevel);
                       mCamera.setParameters(params);
                   }
               }
           });
       }else
         zoomControls1.setVisibility(View.GONE); 
         
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
	              Environment.DIRECTORY_PICTURES), "sana");
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
