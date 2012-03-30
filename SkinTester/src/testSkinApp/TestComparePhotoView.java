package testSkinApp;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;

@SuppressWarnings({ "unchecked", "rawtypes" })

public class TestComparePhotoView extends ActivityInstrumentationTestCase2 {

	private static final String TARGET_PACKAGE_ID="bia.foo";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="bia.foo.ComparePhotoView";
	private static Class launcherActivityClass;
	static{
		try{
			launcherActivityClass=Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		}
		catch (ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
	public TestComparePhotoView()throws ClassNotFoundException{
		super(TARGET_PACKAGE_ID,launcherActivityClass);
	}
	
	private Solo solo;
	
	@Override
	protected void setUp() throws Exception{
		solo = new Solo(getInstrumentation(),getActivity());
	}
	
	//add testing methods here
	public void testDisplayBlackBox(){
		//add specific test inside the proper methods
		//almost all tests in the for of "solo.*"
		//see TestMoleFinderActivity for sample
	}
	
	//end testing methods
	@Override
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
}
