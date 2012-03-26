package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Skin Condition Log
 * Copyright (C) 2012 Andrea Budac, Kurtis Morin, Christian Jukna
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/** 
 * DisplayPhotoView
 * This view is for displaying the photo that is selected.
 * This activity is started from PhotoLayoutView.
 * This activity returns no data.
 * This activity is given the bitmap, folder name and
 * time stamp by the activity that calls it.
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 */

public class DisplayPhotoView extends Activity {
	private ImageView imagePreview;
	private TextView photoGroupName;
	private TextView photoTimeStamp;
	private TextView photoTag;
	private TextView photoAnnotate;
	private Button addAnnotate;
	private Button addTag;
	
	private dbAdapter dbHelper;
	
	static final int DIALOG_NEW_ANNOTATE_ID = 0;
	static final int DIALOG_NEW_TAG_ID = 1;
	
	private String Annotation;
	private String Tag;
	private String rowId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview); 
        
        imagePreview = (ImageView) findViewById(R.id.image2);
        photoGroupName = (TextView) findViewById(R.id.photogroupname);
        photoTimeStamp = (TextView) findViewById(R.id.phototimestamp);
        photoTag = (TextView) findViewById(R.id.phototag);
        photoAnnotate = (TextView) findViewById(R.id.photoannotate);
        addAnnotate = (Button) findViewById(R.id.new_annotation);
        addTag = (Button) findViewById(R.id.new_tag);
        
        dbHelper = new dbAdapter(this);
        
        rowId = (String) getIntent().getStringExtra("rowId");
        
        //Sets the image view to the enlarged bitmap of the photo that
        //was clicked.
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        imagePreview.setImageBitmap(bitmap);
        
        //Set the folder name at top of screen to correct folder.
        String folder = (String) getIntent().getStringExtra("FolderName");
        photoGroupName.setText(folder);
        
        //Set the time stamp at bottom of screen to correct time stamp.
        String time = (String) getIntent().getStringExtra("TimeStamp");
        photoTimeStamp.setText(time);
        
        //Set the time stamp at bottom of screen to correct time stamp.
        String tag = (String) getIntent().getStringExtra("Tag");
        photoTag.setText(tag);
        
        //Set the time stamp at bottom of screen to correct time stamp.
        String annotate = (String) getIntent().getStringExtra("Annotate");
        photoAnnotate.setText(annotate);
        
        
        // Allow user to add annotation to currently displayed photo
		addAnnotate.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				showDialog(DIALOG_NEW_ANNOTATE_ID);
			}
		});
        
		// Allow user to add tag to currently displayed photo
		addTag.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				
			}
		});
    }
	
	@Override
    protected Dialog onCreateDialog(int id) {
		final EditText input = new EditText(DisplayPhotoView.this);
    	switch(id) {
    	case DIALOG_NEW_ANNOTATE_ID:
    		Builder addAnnotateDialog = new AlertDialog.Builder(DisplayPhotoView.this);
    		// do the work to define the addDialog
    		addAnnotateDialog.setView(input);
    		addAnnotateDialog.setTitle("Adding a new folder...")
    		.setMessage("Please specify the folder name to add.")
    		// Setting Positive "Add folder" Button
    		.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking Add folder
    				Annotation = input.getText().toString();
    		
    				dbHelper.addAnnotationToPhoto(Annotation, rowId);
    				
    				photoAnnotate.setText(Annotation);
    				
    				dialog.dismiss();
    				input.setText("");
    			}
    		})

    		// Setting Negative "NO" Button
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				//actions to complete when clicking cancel
    				dialog.dismiss();
    			}
    		});
    		return addAnnotateDialog.create();
    	case DIALOG_NEW_TAG_ID:
    		Builder addTagDialog = new AlertDialog.Builder(this);
    		// do the work to define the addDialog
    		addTagDialog.setView(input);
    		addTagDialog.setTitle("Adding a new folder...")
    		.setMessage("Please specify the folder name to add.")
    		// Setting Positive "Add folder" Button
    		.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking Add folder
    				Tag = input.getText().toString();
    		
    				//dbHelper.createFolder(folderName);
    				
    				dialog.dismiss();
    				input.setText("");
    			}
    		})

    		// Setting Negative "NO" Button
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				//actions to complete when clicking cancel
    				dialog.dismiss();
    			}
    		});
    		return addTagDialog.create();
        }
        return null;
    }
}
