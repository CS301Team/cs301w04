package bia.foo;

import android.app.Activity;
import android.os.Bundle;

public class PhotoView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //insert proper layout here
    }
}
