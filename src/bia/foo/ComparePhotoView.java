package bia.foo;

import android.app.Activity;
import android.os.Bundle;


//This is the view for comparing two photos with eachother
public class ComparePhotoView extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //insert proper layout here
    }
}
