package bia.foo.test;

import bia.foo.DisplayPhotoView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;


/*this tests the photo display, theres nothing to really test until part 4 */

public class DisplayPhotoViewTest extends ActivityInstrumentationTestCase2<DisplayPhotoView> {
	
	private DisplayPhotoView mActivity;
	
	public DisplayPhotoViewTest(){
		super("bia.foo", DisplayPhotoView.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		//initializing the activity
		mActivity = getActivity();
		
	}
	
	@UiThreadTest
	public void testPreconditions(){
		int x = 1;
		assertTrue(x != 1);
	}
	@UiThreadTest
	public void testButtonFunctions(){	

	}
	
	@UiThreadTest
	public void testStateDestroy(){

	}
	
	@UiThreadTest
	public void testStatePause(){

	}

}
