package testSkinApp;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;

@SuppressWarnings({ "unchecked", "rawtypes" })

public class TestMoleFinderActivity extends ActivityInstrumentationTestCase2 {

	private static final String TARGET_PACKAGE_ID="skinConditionsTracker.View";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="skinConditionsTracker.View.FolderLayoutView";
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
	
//	public void testDisplayBlackBox(){
//		solo.clickOnButton("Add Folder");
//		solo.enterText(0, "Test Folder");
//		solo.clickOnButton(0);
//		solo.clickInList(2, 0);
//		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView" );
//		solo.goBack();
//		solo.clickLongInList(2,0);
//		solo.clickOnButton(0);
//		Assert.assertFalse(solo.searchText("Test Folder"));
//		solo.clickInList(1);
//		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView");
//		solo.clickOnScreen(50,250);
//		solo.assertCurrentActivity("Error: Photo Layout not Open", "DisplayPhotoView" );
//		
//	}
	
//	public void testAddFolderStressTest(){
//		int x = 0;
//		while (x < 1000){
//			solo.clickOnButton("Add Folder");
//			solo.enterText(0, "Test Folder"+x);
//			solo.clickOnButton(0);
//			x++;
//		}
//	}
	public void testFolderNames(){
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "Test Folder");
		solo.clickOnButton(0);
		Assert.assertTrue(solo.searchText("Test Folder"));
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "NEWLINE\n\n");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("NEWLINE"));
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "SINGLEQUOTE '' '' '''' '' '");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("SINGLEQUOTE"));
		solo.clickOnButton("Add Folder");
		solo.enterText(0, "TOOLONG asjdhflakjsdhflkajshdlfkjahskjdhflkashjdflakjshdflkjahsdlfkjahsldkjfhalskdj");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("TOOLONG"));
		solo.clickLongInList(2,0);
		solo.clickOnButton(0);
	}
	
	public void testPhotoDisplay(){
		solo.clickInList(1);
		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView");
		solo.clickOnScreen(50,250);
		solo.assertCurrentActivity("Error: Photo Layout not Open", "DisplayPhotoView" );
		solo.clickOnButton("Modify Annotation");
		solo.enterText(0, "TOOLONG asjdhflakjsdhflkajshdlfkjahskjdhf" +
				"lkashjdflakjshdflkjahsdlfkjahsldkjfhalskdjafsdfsdfasd" +
				"fasdfasdfasdfasdfasdfasdfasdfasdfasdfdfasdfasdfasdfasdadfhadsfgdfgasdfashasdfasdgasdfgahaerg" +
				"dgasdgasdgasdgasdgasdfgawefgasdgasdgasdfbadfgadf" +
				"asfhlkasjdfhuweifhaskjdhvuawefhlkasjhlasdkgjashdkjgsdf" +
				"dfhajksdfhlkajsdhguehwasdlgasdgjkasdf" +
				"alshkdjfhlakjsdhlfuasekfluasef" +
				"sdfashldkfuhaueaksfuelasklesleukasdfasdfasdfasdf");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("TOOLONG"));
		solo.clickOnButton("Modify Annotation");
		solo.enterText(0, "SINGLEQUOTE ' ' '' ' '''' ");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("SINGLEQUOTE"));
		solo.clickOnButton("Modify Annotation");
		solo.enterText(0, "Test Annotation");
		solo.clickOnButton(0);
		Assert.assertTrue(solo.searchText("Test Annotation"));
		
		solo.clickOnButton("Modify Tag");
		solo.enterText(0, "TOOLONGTAG asjdhflakjsdhflkajshdlfkjahskjdhf" +
				"lkashjdflakjshdflkjahsdlfkjahsldkjfhalskdjafsdfsdfasd" +
				"fasdfasdfasdfasdfasdfasdfasdfasdfasdfdfasdfasdfasdfasdadfhadsfgdfgasdfashasdfasdgasdfgahaerg" +
				"dgasdgasdgasdgasdgasdfgawefgasdgasdgasdfbadfgadf" +
				"asfhlkasjdfhuweifhaskjdhvuawefhlkasjhlasdkgjashdkjgsdf" +
				"dfhajksdfhlkajsdhguehwasdlgasdgjkasdf" +
				"alshkdjfhlakjsdhlfuasekfluasef" +
				"sdfashldkfuhaueaksfuelasklesleukasdfasdfasdfasdf");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("TOOLONGTAG"));
		solo.clickOnButton("Modify Tag");
		solo.enterText(0, "SINGLEQUOTETAG ' ' '' ' '''' ");
		solo.clickOnButton(0);
		Assert.assertFalse(solo.searchText("SINGLEQUOTETAG"));
		solo.clickOnButton("Modify Tag");
		solo.enterText(0, "Test Tag");
		solo.clickOnButton(0);
		Assert.assertTrue(solo.searchText("Test Tag"));
	}
	
	public void testComparePhoto(){
		solo.clickInList(1);
		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView");
		solo.clickOnButton("Compare");
		solo.clickOnScreen(50,250);
		solo.clickOnScreen(250,250);
		solo.assertCurrentActivity("Error: ComparePhoto Not Open", "ComparePhotoView");
	}
	
	public void testTagFilter(){
		solo.clickInList(1);
		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView");
		solo.clickOnScreen(50,250);
		solo.assertCurrentActivity("Error: Display Photo View not Open", "DisplayPhotoView");
		solo.clickOnButton("Modify Tag");
		solo.enterText(0, "TestTag");
		solo.clickOnButton(0);
		solo.goBack();
		solo.assertCurrentActivity("Error: Photo Layout not Open", "PhotoLayoutView");
		solo.clickOnButton("Filter by Tag");
		solo.pressSpinnerItem(0, 1);
		solo.clickOnButton(0);
		solo.clickOnScreen(50,250);
		solo.assertCurrentActivity("Error: Display Photo View not Open", "DisplayPhotoView");
		Assert.assertTrue(solo.searchText("TestTag"));
		
		
	}
	
	@Override
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
}
