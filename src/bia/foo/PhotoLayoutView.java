package bia.foo;

import android.app.Activity;
import android.os.Bundle;

public class PhotoLayoutView extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photogridview);
    }
}