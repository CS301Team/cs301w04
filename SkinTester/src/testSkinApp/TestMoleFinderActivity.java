package testSkinApp;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;

@SuppressWarnings({ "unchecked", "rawtypes" })

public class TestMoleFinderActivity extends ActivityInstrumentationTestCase2 {

	private static final String TARGET_PACKAGE_ID="bia.foo";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="bia.foo.MoleFinderActivity";
	private static Class launcherActivityClass;
	static{
		try{
			launcherActivityClass=Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		}
		catch (ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
	public TestMoleFinderActivity()throws ClassNotFoundException{
		super(TARGET_PACKAGE_ID,launcherActivityClass);
	}
	
	private Solo solo;
	
	@Override
	protected void setUp() throws Exception{
		solo = new Solo(getInstrumentation(),getActivity());
	}
	
	public void testDisplayBlackBox(){
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "Test Folder");
		solo.clickOnButton(0);
		solo.clickInList(0);
		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView" );
		solo.goBack();
		solo.clickLongInList(0);
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("Test Folder"));
	}
	
	public void testAddDeleteFolder(){
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "Test Folder");
		solo.clickOnButton(0);
		Assert.assertTrue(solo.searchText("Test Folder"));
		solo.clickLongInList(0);
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("Test Folder"));

	}
	
	@Override
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
}
