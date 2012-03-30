package bia.foo;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * It is called by PhotoLayoutView. It takes in two row ids.
 * It does not return anything.
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
	private Cursor cursor;
	private long rowIdOne;
	private long rowIdTwo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparephotos);
        
        dbHelper = new dbAdapter(this);
        
        rowIdOne = getIntent().getLongExtra("RowIdOne", 0);
        rowIdTwo = getIntent().getLongExtra("RowIdTwo", 0);
        
        imagePreview1 = (ImageView) findViewById(R.id.compareView1);
        photoGroupName1 = (TextView) findViewById(R.id.textView1);
        
        imagePreview2 = (ImageView) findViewById(R.id.compareView2);
        photoGroupName2 = (TextView) findViewById(R.id.textView2);
    }
	
	/**
	 * Recreate the database on start to prevent errors
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
		dbHelper.open();
		
		// Call cursor for first photo
		cursor = dbHelper.fetchPhoto(rowIdOne);
		//Sets the image view to the enlarged bitmap of the photo that
        //was clicked.
		byte[] photoOne = cursor.getBlob(cursor.getColumnIndex(dbAdapter.PHOTO));
		Bitmap bitmapOne = BitmapFactory.decodeByteArray(photoOne, 0, photoOne.length);
		bitmapOne.setDensity(10);
        imagePreview1.setImageBitmap(bitmapOne);
        //Set the folder name at top of screen to correct folder.
        String dateOne = cursor.getString(cursor.getColumnIndex(dbAdapter.DATE));
        photoGroupName1.setText(dateOne);
        
        // Update cursor for second photo
        cursor = dbHelper.fetchPhoto(rowIdTwo);
        //Sets the image view to the enlarged bitmap of the photo that
        //was clicked.        
		byte[] photoTwo = cursor.getBlob(cursor.getColumnIndex(dbAdapter.PHOTO));
		Bitmap bitmapTwo = BitmapFactory.decodeByteArray(photoTwo, 0, photoTwo.length);
		bitmapTwo.setDensity(10);
        imagePreview2.setImageBitmap(bitmapTwo);
        //Set the folder name at middle of screen to correct folder.
        String dateTwo = cursor.getString(cursor.getColumnIndex(dbAdapter.DATE));
        photoGroupName2.setText(dateTwo);		
	}

	/**
	 * Close the database on stop to prevent errors
	 * 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		cursor.close();
		dbHelper.close();
	}
}
