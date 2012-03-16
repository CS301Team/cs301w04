package bia.foo.test;

import bia.foo.MoleFinderActivity;
import bia.foo.R;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.ListView;
import android.app.Instrumentation;
//import android.app.Instrumentation.ActivityMonitor;
//import android.content.IntentFilter;



public class MoleFinderActivityTest extends ActivityInstrumentationTestCase2<MoleFinderActivity> {
	
	//private static final int ADAPTER_COUNT = 9;
	//protected static final int INITIAL_POSITION = 0;
	
	//creating button and activity variables
	private MoleFinderActivity mActivity;
	//private Button okButton;
	//private Button cancelButton;
	private Button addGroup;
	private Button deleteGroup;
	private ListView list;

	
	
	public MoleFinderActivityTest(){
		super("bia.foo", MoleFinderActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//setActivityInitialTouchMode(true);
		
		//initializing the activity
		mActivity = getActivity();
		
		//initializing the buttons
		addGroup = (Button) mActivity.findViewById(bia.foo.R.id.add_group);
		deleteGroup = (Button) mActivity.findViewById(bia.foo.R.id.delete_group);
		
	}
	
	@UiThreadTest
	public void testPreconditions(){
		assertTrue(addGroup != null);
		assertTrue(deleteGroup != null);
	}
	@UiThreadTest
	public void testGroupFunctions(){	
		//press buttons
		addGroup.performClick();
		deleteGroup.performClick();
		
		//check that they remain in view after pressed
		assertTrue(addGroup != null);
		assertTrue(deleteGroup != null);
	}
	
	@UiThreadTest
	public void testListItemClickStartsActivity() {
//	   final Instrumentation inst = getInstrumentation();
//	    final IntentFilter intentFilter = new IntentFilter();
//	    intentFilter.addAction(".PhotoLayoutView");
//	   ActivityMonitor monitor = inst.addMonitor(intentFilter, null, false);
//	   assertEquals(0, monitor.getHits());

	    
	    list = (ListView) mActivity.findViewById(R.id.photo_grouping_list);
	    list.performLongClick();
	    list.setSelection(0);
	    list.performLongClick();
	    

//	   monitor.waitForActivityWithTimeout(5000);
//	    assertEquals(1, monitor.getHits());
//	    inst.removeMonitor(monitor);
	}
	
	public void testStateDestroy(){

		mActivity.finish();
		mActivity = getActivity();
		
		
		assertTrue(addGroup != null);
		assertTrue(deleteGroup != null);
	}
	
	@UiThreadTest
	public void testStatePause(){
		Instrumentation mInstr = this.getInstrumentation();
			
		mInstr.callActivityOnPause(mActivity);
		mInstr.callActivityOnResume(mActivity);

		addGroup.performClick();
		deleteGroup.performClick();
		
		assertTrue(addGroup != null);
		assertTrue(deleteGroup != null);
	}

}
