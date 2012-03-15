package bia.foo;

import android.app.Activity;
import android.os.Bundle;

//this view is for displaying the photo that is selected
public class DisplayPhotoView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview); 
        
        
    }
}
