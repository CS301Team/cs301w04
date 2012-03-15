package bia.foo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

//this view is for displaying the photo that is selected
public class DisplayPhotoView extends Activity {
	private ImageView imagePreview;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview); 
        
        imagePreview = (ImageView) findViewById(R.id.image2);
        
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        imagePreview.setImageBitmap(bitmap);
    }
}
