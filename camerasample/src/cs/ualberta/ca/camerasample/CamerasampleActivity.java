package cs.ualberta.ca.camerasample;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CamerasampleActivity extends Activity {
	
	private Uri imageUri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
        
        OnClickListener listener = new OnClickListener(){
        	

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takeAPhoto();
			}
        };
        
        button.setOnClickListener(listener);
    
    }
    
    protected void takeAPhoto(){
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
    	
    	File folderF = new File(folder);
    	
    	if(!folderF.exists()){
    		folderF.mkdir();
    	}
    	
    	String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
    	
    	File imageFile = new File(imageFilePath);
    	
    	imageUri = Uri.fromFile(imageFile);
    	
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    	
    	startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    	
    	
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			
			if(resultCode == RESULT_OK){
				
				ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
				button.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
				
			}
		}
	}
    
    
    
}