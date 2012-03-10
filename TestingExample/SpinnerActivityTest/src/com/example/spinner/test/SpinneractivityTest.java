package com.example.spinner.test;

import com.example.spinner.SampleSpinnerActivity;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SpinneractivityTest extends
		ActivityInstrumentationTestCase2<SampleSpinnerActivity> {
	
	private static final int ADAPTER_COUNT = 9;
	protected static final int INITIAL_POSITION = 0;
	private SampleSpinnerActivity mActivity;
	private Spinner mSpinner;
	private SpinnerAdapter mPlanetData;
	private String mSelection;
	private int mPos;
	
	public SpinneractivityTest(){
		super("com.example.spinner", SampleSpinnerActivity.class);
	}
	
	//connect to the ui throught the main.xml layout objects
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
		
		mSpinner = (Spinner) mActivity.findViewById(com.example.spinner.R.id.Spinner01);
		
		mPlanetData = mSpinner.getAdapter();
	}
	
	public void testPreconditions(){
		assertTrue(mSpinner.getOnItemSelectedListener() != null);
		assertTrue(mPlanetData != null);
		assertEquals(mPlanetData.getCount(), ADAPTER_COUNT);
	}
	
	public void testSpinnerConditions(){
		
		mActivity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				mSpinner.requestFocus();
				mSpinner.setSelection(INITIAL_POSITION);
			}
			
		});
		
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		
		mPos = mSpinner.getSelectedItemPosition();
		mSelection = (String) mSpinner.getItemAtPosition(mPos);
		
		TextView resultView = (TextView) mActivity.findViewById(
				com.example.spinner.R.id.SpinnerResult);
		String resultText = (String) resultView.getText();
		assertEquals(resultText, mSelection);
		
	}
	
	private static final int TEST_STATE_DESTROY_POSITION = 2;
	private static final String TEST_STATE_DESTROY_SELECTION = "Earth";
	private static final int TEST_STATE_PAUSE_POSITION = 4;
	private static final String TEST_STATE_PAUSE_SELECTION = "Jupiter";
	
	public void testStateDestroy(){
		mActivity.setSpinnerPosition(TEST_STATE_DESTROY_POSITION);
		mActivity.setSpinnerSelection(TEST_STATE_DESTROY_SELECTION);
		mActivity.finish();
		mActivity = getActivity();
		
		int currentPosition = mActivity.getSpinnerPosition();
		String currentSelection = mActivity.getSpinnerSelection();
		assertEquals(TEST_STATE_DESTROY_POSITION, currentPosition);
		assertEquals(TEST_STATE_DESTROY_SELECTION, currentSelection);
	}
	
	@UiThreadTest
	public void testStatePause(){
		Instrumentation mInstr = this.getInstrumentation();
		
		mActivity.setSpinnerPosition(TEST_STATE_PAUSE_POSITION);
		mActivity.setSpinnerSelection(TEST_STATE_PAUSE_SELECTION);
		
		mInstr.callActivityOnPause(mActivity);
		
		mActivity.setSpinnerPosition(0);
		mActivity.setSpinnerSelection("");
		
		mInstr.callActivityOnResume(mActivity);
		
		int currentPosition = mActivity.getSpinnerPosition();
		String currentSelection = mActivity.getSpinnerSelection();
		
		assertEquals(TEST_STATE_PAUSE_POSITION, currentPosition);
		assertEquals(TEST_STATE_PAUSE_SELECTION, currentSelection);
	}
	
}
