package bia.foo.test;

import bia.foo.PhotoLayoutView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.app.Instrumentation;


/*this tests the photo layout view buttons and activity launching */

public class PhotoLayoutViewTest extends ActivityInstrumentationTestCase2<PhotoLayoutView> {
	
	//creating button and activity variables
	private PhotoLayoutView mActivity;
	private Button takePhoto;
	private Button deletePhoto;

	
	
	public PhotoLayoutViewTest(){
		super("bia.foo", PhotoLayoutView.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		//initializing the activity
		mActivity = getActivity();
		
		//initializing the buttons
		takePhoto = (Button) mActivity.findViewById(bia.foo.R.id.new_photo);
		deletePhoto = (Button) mActivity.findViewById(bia.foo.R.id.delete_photo);
		
	}
	
	@UiThreadTest
	public void testPreconditions(){
		assertTrue(takePhoto != null);
		assertTrue(deletePhoto != null);
	}
	@UiThreadTest
	public void testButtonFunctions(){	
		//press buttons
		//takePhoto.performClick();
		//deletePhoto.performClick();
		
		//check that they remain in view after pressed
		assertTrue(takePhoto != null);
		//mActivity = getActivity();
		//mActivity.finish();
		
		assertTrue(deletePhoto != null);
		//testing button functions postponed to part 3
		int x=1;
		assertTrue(x!=1);
		assertTrue(x!=1);
	}
	
	
	@UiThreadTest
//	public void testListItemClickStartsActivity() {
//	   final Instrumentation inst = getInstrumentation();
//    final IntentFilter intentFilter = new IntentFilter();
////	    intentFilter.addAction(".PhotoLayoutView");
//	   ActivityMonitor monitor = inst.addMonitor(intentFilter, null, false);
//	   assertEquals(0, monitor.getHits());
//
//	    
//	    
//	    mActivity.finish();
//	    
//
//	   monitor.waitForActivityWithTimeout(5000);
//	    assertEquals(1, monitor.getHits());
//	    inst.removeMonitor(monitor);
//	}
	
	public void testStateDestroy(){

		mActivity.finish();
		mActivity = getActivity();
		
		
		assertTrue(takePhoto != null);
		assertTrue(deletePhoto != null);
	}
	
	@UiThreadTest
	public void testStatePause(){
		Instrumentation mInstr = this.getInstrumentation();
			
		mInstr.callActivityOnPause(mActivity);
		mInstr.callActivityOnResume(mActivity);

		//takePhoto.performClick();
		//deletePhoto.performClick();
		
		assertTrue(takePhoto != null);
		assertTrue(deletePhoto != null);
	}

}
