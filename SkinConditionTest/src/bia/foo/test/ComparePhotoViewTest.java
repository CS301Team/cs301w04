package bia.foo.test;

import bia.foo.ComparePhotoView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;


/*this tests the photo comparision display, theres nothing to really test until part 4 */

public class ComparePhotoViewTest extends ActivityInstrumentationTestCase2<ComparePhotoView> {
	
	private ComparePhotoView mActivity;
	
	public ComparePhotoViewTest(){
		super("bia.foo", ComparePhotoView.class);
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
	
	
	public void testStateDestroy(){

	}
	
	@UiThreadTest
	public void testStatePause(){

	}

}
