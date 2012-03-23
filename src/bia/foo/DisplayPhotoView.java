package bia.foo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photopreview); 
        
        imagePreview = (ImageView) findViewById(R.id.image2);
        photoGroupName = (TextView) findViewById(R.id.photogroupname);
        photoTimeStamp = (TextView) findViewById(R.id.phototimestamp);
        photoTag = (TextView) findViewById(R.id.phototag);
        photoAnnotate = (TextView) findViewById(R.id.photoannotate);
        
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
    }
}
