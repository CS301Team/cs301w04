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
 * ComparePhotoView
 * This is the view for comparing two photos with each other.
 * this is not implemented yet
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 * 
 */

public class ComparePhotoView extends Activity{
	private ImageView imagePreview1;
	private TextView photoGroupName1;
	private ImageView imagePreview2;
	private TextView photoGroupName2;
	
	private dbAdapter dbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparephotos);

        
        imagePreview1 = (ImageView) findViewById(R.id.compareView1);
        photoGroupName1 = (TextView) findViewById(R.id.textView1);
        
        imagePreview2 = (ImageView) findViewById(R.id.compareView2);
        photoGroupName2 = (TextView) findViewById(R.id.textView2);
        
        Bitmap bitmap1 = (Bitmap) getIntent().getParcelableExtra("BitmapImage1");
        imagePreview1.setImageBitmap(bitmap1);
        
        //Set the folder name at top of screen to correct folder.
        String folder1 = (String) getIntent().getStringExtra("Date1");
        photoGroupName1.setText(folder1);
        
        Bitmap bitmap2 = (Bitmap) getIntent().getParcelableExtra("BitmapImage2");
        imagePreview2.setImageBitmap(bitmap2);
        
        //Set the folder name at top of screen to correct folder.
        String folder2 = (String) getIntent().getStringExtra("Date2");
        photoGroupName2.setText(folder2);
    }
	
	/**
	 * Recreate the database on start to prevent errors
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();

		dbHelper.open();
        //fillData();
	}

	/**
	 * Close the database on stop to prevent errors
	 * 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		dbHelper.close();
		//cursor.close();
	}
}
