package skinConditionsTracker.View;

import skinConditionsTracker.Controller.DatabaseAdapter;
import skinConditionsTracker.Model.R;
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
 * April 06, 2012
 * 
 */

public class ComparePhotoView extends Activity{
	private ImageView imagePreview1;
	private TextView photoGroupName1;
	private ImageView imagePreview2;
	private TextView photoGroupName2;

	private long rowIdOne;
	private long rowIdTwo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparephotos);

        rowIdOne = getIntent().getLongExtra("RowIdOne", 0);
        rowIdTwo = getIntent().getLongExtra("RowIdTwo", 0);
        
        imagePreview1 = (ImageView) findViewById(R.id.compareView1);
        photoGroupName1 = (TextView) findViewById(R.id.textView1);
        
        imagePreview2 = (ImageView) findViewById(R.id.compareView2);
        photoGroupName2 = (TextView) findViewById(R.id.textView2);
    }
	
	/**
	 * Creates a database where it gets two bitmaps and sets them as
	 * the imageviews to be displayed. It gets these bitmaps from the
	 * rowIDs passed into the activity from selecting photos.
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
		DatabaseAdapter dbHelper;
		dbHelper = new DatabaseAdapter(this);
		dbHelper.open();
		
		Cursor cursor;
		// Call cursor for first photo
		cursor = dbHelper.fetchPhoto(rowIdOne);
		//Sets the image view to the enlarged bitmap of the photo that
        //was clicked.
		byte[] photoOne = cursor.getBlob(cursor.getColumnIndex(DatabaseAdapter.PHOTO));
		Bitmap bitmapOne = BitmapFactory.decodeByteArray(photoOne, 0, photoOne.length);
		bitmapOne.setDensity(10);
        imagePreview1.setImageBitmap(bitmapOne);
        //Set the folder name at top of screen to correct folder.
        String dateOne = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.DATE));
        photoGroupName1.setText(dateOne);
        
        cursor.close();
        
        // Update cursor for second photo
        cursor = dbHelper.fetchPhoto(rowIdTwo);
        //Sets the image view to the enlarged bitmap of the photo that
        //was clicked.        
		byte[] photoTwo = cursor.getBlob(cursor.getColumnIndex(DatabaseAdapter.PHOTO));
		Bitmap bitmapTwo = BitmapFactory.decodeByteArray(photoTwo, 0, photoTwo.length);
		bitmapTwo.setDensity(10);
        imagePreview2.setImageBitmap(bitmapTwo);
        //Set the folder name at middle of screen to correct folder.
        String dateTwo = cursor.getString(cursor.getColumnIndex(DatabaseAdapter.DATE));
        photoGroupName2.setText(dateTwo);
        
        cursor.close();
        dbHelper.close();
	}
}
