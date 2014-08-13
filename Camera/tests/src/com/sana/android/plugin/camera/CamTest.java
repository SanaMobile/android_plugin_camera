package com.sana.android.plugin.camera;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;
import com.sana.android.plugin.camera.MainActivity;


public class CamTest extends ActivityInstrumentationTestCase2<MainActivity> {

	MainActivity activity;
	private Instrumentation mInstrumentation;
	ImageButton button;
	
	@SuppressWarnings("deprecation")
	public CamTest(String name) {
		super("com.sana.android.plugin.camera",MainActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();this.mInstrumentation = getInstrumentation();
		Intent intent = new Intent(mInstrumentation.getTargetContext(),MainActivity.class);
		intent.putExtra("flash","yes");
		intent.putExtra("white", "yes");
	    intent.putExtra("shutter", "yes");
	    intent.putExtra("quality", "152");
	    setActivityIntent(intent);
        activity = this.getActivity(); 
	}
	
	/*public void testViewsCreated() {
	    assertNotNull(activity);
	}
	*/
	public void testViewsButtonCapture(){
		button = (ImageButton)activity.findViewById(com.sana.android.plugin.camera.R.id.imageButton1);
		assertNotNull(activity.findViewById(com.sana.android.plugin.camera.R.id.imageButton1));
	    
		activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                button.performClick();
            }
        });
	    assertNotNull(button);
	}

	
}
