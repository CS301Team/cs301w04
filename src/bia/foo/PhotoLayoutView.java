package bia.foo;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
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
 * PhotoLayoutView
 * This view allows the user to view photos in a selected folder.
 * The folder name is given to this activity from MoleFinderActivity.
 * This activity calls DisplayPhotoView, when a photo is clicked.
 * This activity passes a bitmap, folder name and time stamp of
 * the clicked photo to DisplayPhotoView.
 * 
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 */

public class PhotoLayoutView extends Activity
{
	private static final int CAMERA_PHOTO_REQUEST = 100;
	
	private Button newPhoto;
	private Button delButton;
	private Button compPhoto;
	private TextView currentFolder;
	private GridView gridView;
	//
	public Bitmap currBitmap;
	//private Intent intent;
	
	private int compare_counter;
	
	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
    
    public String folderName;
    
    static final int DIALOG_DELETE_PHOTO_ID = 0;
	
	/** onCreate called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogridview);
		
		newPhoto = (Button) findViewById(R.id.new_photo);
		delButton = (Button) findViewById(R.id.delete_photo);
		compPhoto = (Button) findViewById(R.id.compare_photo);
        gridView = (GridView) findViewById(R.id.gridView1);
        currentFolder = (TextView) findViewById(R.id.current_folder);
        final Intent intent = new Intent(PhotoLayoutView.this, ComparePhotoView.class);
        compare_counter = 0;
        
		dbHelper = new dbAdapter(this);
        dbHelper.open();
        
        folderName = getIntent().getStringExtra("FolderName");
        currentFolder.setText(folderName);
        
        fillData();
		
		/** 
		 * When newPhoto button is clicked, call the method
		 * takeAPhoto
		 * @see takeAPhoto
		 */
		newPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takeAPhoto();
			}
		});
		
		/** 
		 * When delButton is clicked a dialog
		 * appears asking for confirmation. If yes is selected
		 * then the last selected photo is deleted. If no is selected
		 * then it closes the dialog.
		 * @see onCreateDialog
		 */
		delButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DELETE_PHOTO_ID);
    		}
		});
		
		compPhoto.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(compare_counter == 0){
//					ImageView imageView = (ImageView) v.findViewById(R.id.image1); 
//					BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//					Bitmap bitmap = drawable.getBitmap();
					
					intent.putExtra("BitmapImage1", currBitmap);
					
					intent.putExtra("FolderName1", folderName);
					compare_counter++;
				}
				else{
					intent.putExtra("BitmapImage2", currBitmap);
					
					intent.putExtra("FolderName2", folderName);
					compare_counter=0;
					startActivity(intent);
				}
				
			}
		});
		
		/** 
		 * When item is clicked in the GridView, it's id is recorded.
		 */
		gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				entryID = id;
				//Bitmap currBitmap;
				//for the comparing
				ImageView imageView = (ImageView) v.findViewById(R.id.image1); 
				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
				currBitmap = drawable.getBitmap();
				//currBitmap = bitmap;
			}
		});
		
		/**
		 *  When item is long clicked in the GridView it gets the bitmap of the image
		 *  along with the foldername the item belongs to and the timestamp of the item.
		 *  An activity to DisplayPhotoView is then called.
		 *  @return true
		 */
		gridView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				ImageView imageView = (ImageView) v.findViewById(R.id.image1); 
				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				
				//Fetch the date of the selected photo
				Cursor cursor = dbHelper.fetchPhoto(id);
				String date = cursor.getString(cursor.getColumnIndex(dbAdapter.DATE));
				
				Intent intent = new Intent(PhotoLayoutView.this, DisplayPhotoView.class);
				intent.putExtra("BitmapImage", bitmap);
				
				intent.putExtra("FolderName", folderName);
				intent.putExtra("TimeStamp", date);
				
				startActivity(intent);
				
				return true;
			}
		});
	}
	
	
	/**
	 * takeAPhoto launches a new camera activity.
	 */
    protected void takeAPhoto() {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	startActivityForResult(intent, CAMERA_PHOTO_REQUEST);
    }
    
    
	/**
	 * Call to onActivityResult, if takeAPhoto() exited correctly, 
	 * then put returned data into the database
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {	
		if (requestCode == CAMERA_PHOTO_REQUEST) {		
			if (resultCode == RESULT_OK) {
				super.onActivityResult(requestCode, resultCode, intent);
				
				String date = DateFormat.getDateInstance().format(new Date());
				String folder = folderName;
				
				Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
				byte[] photo = baos.toByteArray();
				
				dbHelper.createPhotoEntry(date, folder, photo);
				fillData();
			}
		}
	}
	
	/**
	 * fillData() recreates the gridView and updates the list.
	 */
	private void fillData() {
        // Get all of the notes from the database and create the item list
        entriesCursor = dbHelper.fetchPhotosInFolder(folderName);
        startManagingCursor(entriesCursor);
        
        // Create an array to specify the fields we want to display in the list (only DATE)
        String[] from = new String[] { dbAdapter.PHOTO, dbAdapter.DATE};
        int[] to = new int[] { R.id.image1, R.id.text1 };
        
        // Create an array adapter and set it to display
        SimpleCursorAdapter entries =
        		new SimpleCursorAdapter(this, R.layout.entry_row, entriesCursor, from, to);                         
		
        entries.setViewBinder(new PhotoViewBinder());
        
        Log.w("NumRows", entries.getCount() + "");
        
        gridView.setAdapter(entries);
    }

	/** OnCreateDialog method to create the dialogs when called.
     * Uses a switch case mechanism to decide which dialog to display.
     * @param int id (Used for the switch/case)
     * When the ID is delete photo a confirmation dialog appears asking the
     * user to make their final decision. They can confirm the delete or cancel.
     * The function returns the created dialog.
     * @return dialog.create() */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DIALOG_DELETE_PHOTO_ID:
			Builder deleteDialog = new AlertDialog.Builder(PhotoLayoutView.this);
			deleteDialog.setTitle("Confirm Delete...")
			.setMessage("Are you sure you want to delete this photo?")
			// do the work to define the deleteDialog
			// Setting Positive "Yes" Button
			.setPositiveButton("Delete Photo", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					//actions to complete when clicking yes

					if(entryID != -1) {
						dbHelper.deletePhoto(entryID);
						fillData();
					}

					dialog.dismiss();
				}
			})

			// Setting Negative "NO" Button
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//actions to complete when clicking cancel
					dialog.dismiss();
				}
			});
			return deleteDialog.create();
		}
		return null;
	}
}