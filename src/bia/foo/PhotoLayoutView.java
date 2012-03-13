package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//this view is a grid of photos inside each group
public class PhotoLayoutView extends Activity {
	private Button deletePhotoButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photogridview);
        
        deletePhotoButton = (Button) findViewById(R.id.delete_photo);
        
      //creation of the dialog box confirming item deletion
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(PhotoLayoutView.this);
        // Setting Dialog Title
        deleteDialog.setTitle("Confirm Delete...");
        // Setting Dialog Message
        deleteDialog.setMessage("Are you sure you want to delete this photo?");
        
     // Setting Positive "Yes" Button
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int which) {
        		//actions to complete when clicking yes
        	}
        });

        // Setting Negative "NO" Button
        deleteDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		//actions to complete when clicking cancel
        		dialog.cancel();
        	}
        });

        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		if () {
//        			// brings up the dialog box to confirm deletion
        			deleteDialog.show();
//        		} else {
//        			// if no item has been selected from the list
//        		}
        	}
        });
    }
}
