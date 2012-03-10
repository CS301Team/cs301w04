package bia.foo;

import android.app.Activity;
import android.os.Bundle;

public class DisplayPhotoView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview); 
    }
}
