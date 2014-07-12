package com.example.cam;


import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ZoomControls;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = null;
	private SurfaceHolder mHolder;
    private Camera mCamera;
    Camera.Parameters params;
    private CameraPreview mPreview;
    

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
    	try {
            // This case can actually happen if the user opens and closes the camera too frequently.
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch(Exception e) {
            Log.e(TAG, "Surfacecreatederror");
        }
        
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Take care of releasing the Camera preview in your activity.
    	if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
      Log.e("surfaceDestroyed", "surfaceDestroyed");

    }
    
    public void surfacePaused(SurfaceHolder holder) {
        // Take care of releasing the Camera preview in your activity.
    	Log.e("TABACT", "surfacepaused");
    	mPreview.setVisibility(View.INVISIBLE);
    }

    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        // ignore: tried to stop a non-existent preview
        
        } catch (Exception e){
    	Log.d(TAG, "zoom error: " + e.getMessage());
    }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
        	mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
        }
    
    
    public static void setCameraDisplayOrientation(Activity activity,
            int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();
        
            mCamera = null;
        }
    }
}
