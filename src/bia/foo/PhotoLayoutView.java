package bia.foo;

import bia.foo.PhotoHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
 * Friday, March 30, 2012
 * 
 */

//http://www.dibbus.com/2011/02/gradient-buttons-for-android/
//http://www.typeoneerror.com/articles/post/android-changing-listview-background-colors
//http://openiconlibrary.sourceforge.net/

public class PhotoLayoutView extends Activity
{
	private static final int CAMERA_PHOTO_REQUEST = 100;
	
	private Button newPhoto;
	private Button compPhoto;
	private Button sortByTag;
	private TextView currentFolder;
	private GridView gridView;

	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
    
    private String folderName;
    private String _path;
    
    private PhotoHolder pHolder;
    
    boolean comparePhotoIsSet = false;
    
    static final int DIALOG_DELETE_PHOTO_ID = 0;
    static final int DIALOG_SORT_TAG_ID = 1;
	
	/** onCreate called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogridview);
		
		newPhoto = (Button) findViewById(R.id.new_photo);
		compPhoto = (Button) findViewById(R.id.compare_photo);
		sortByTag = (Button) findViewById(R.id.sort_tag);
        gridView = (GridView) findViewById(R.id.gridView1);
        currentFolder = (TextView) findViewById(R.id.current_folder);
        final Intent intent = new Intent(PhotoLayoutView.this, ComparePhotoView.class);
        
        ///
        ///
        _path = Environment.getExternalStorageDirectory() + "/images/temp_photo_holder.jpg";
        ///
        
        pHolder = new PhotoHolder();
        
		dbHelper = new dbAdapter(this);
        
        folderName = getIntent().getStringExtra("FolderName");
        currentFolder.setText(folderName);
        
		/** 
		 * When newPhoto button is clicked, call the method
		 * takeAPhoto
		 * @see takeAPhoto
		 */
		newPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takeAPhoto();
			}
		});
		
		
		compPhoto.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(comparePhotoIsSet == false){
					comparePhotoIsSet = true;
					comparePhotoToast();
				}
				else if (comparePhotoIsSet == true){
					comparePhotoIsSet = false;
					pHolder.clearPhotoHolder(pHolder);
					cancelCompareToast();
				}
			}
		});
		
		sortByTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				showDialog(DIALOG_SORT_TAG_ID);
				Builder sortDialog = new AlertDialog.Builder(PhotoLayoutView.this);
				
				LayoutInflater inflater = LayoutInflater.from(PhotoLayoutView.this);
				final View layout = inflater.inflate(R.layout.tag_spinner, null);
				sortDialog.setView(layout);
				Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);
				
				sortDialog.setTitle("Tag selection...")
				.setMessage("Select tag to view photos from that group.")
				// do the work to define the sortDialog
				// Setting Neutral Button
				.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						//actions to complete when clicking neutral
						dialog.dismiss();
					}
				});
				
				Cursor tagCursor = dbHelper.fetchUniqueTags();
				startManagingCursor(tagCursor);
				
//				String[] from = new String[] {dbAdapter.TAG };
//				int[] to = new int [] {android.R.id.text1 };
//				
//				SimpleCursorAdapter adapter = 
//					new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, tagCursor, from, to);
				//			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//			spinner.setAdapter(adapter);


				final String def = "Default Folder Photos";

				ArrayList <String> tagList = new ArrayList<String>();
				tagList.add(def);
				if (tagCursor != null) {
					if (tagCursor.moveToFirst()) {
						do {
							String tagName = tagCursor.getString(tagCursor.getColumnIndex("tag"));
							if (!tagName.equals("")) {
								tagList.add(tagName); 
							}
						} while (tagCursor.moveToNext());
					}
				}
				
				ArrayAdapter<String> adapter =  new ArrayAdapter<String>(PhotoLayoutView.this, android.R.layout.simple_spinner_item, tagList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
				
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected (AdapterView<?> parent, View v, int position, long id) {
						String selectedTag = ((TextView)v.findViewById(android.R.id.text1)).getText().toString();
						if (!selectedTag.equals(def)) {
							Cursor tagCursor = dbHelper.fetchPhotosUnderTag(selectedTag);
							
							startManagingCursor(tagCursor);
					        
					        // Create an array to specify the fields we want to display in the list (only DATE)
					        String[] from = new String[] { dbAdapter.PHOTO, dbAdapter.DATE};
					        int[] to = new int[] { R.id.image1, R.id.text1 };
					        
					        // Create an array adapter and set it to display
					        SimpleCursorAdapter entries =
					        		new SimpleCursorAdapter(PhotoLayoutView.this, R.layout.entry_row, tagCursor, from, to);                         
							
					        entries.setViewBinder(new PhotoViewBinder());
					        
					        gridView.setAdapter(entries);	
						} else {
							fillData();
						}
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						
					}
				});
				
				tagCursor.close();
				sortDialog.show();
			}
		});
		
		/** 
		 *  When item is clicked in the GridView it gets the bitmap of the image
		 *  along with the foldername the item belongs to and the timestamp of the item.
		 *  An activity to DisplayPhotoView is then called.
		 */
		gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				ImageView imageView = (ImageView) v.findViewById(R.id.image1); 
				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				
				Cursor cursor = dbHelper.fetchPhoto(id);
				String date = cursor.getString(cursor.getColumnIndex(dbAdapter.DATE));
				String tag = cursor.getString(cursor.getColumnIndex(dbAdapter.TAG));
				String annotate = cursor.getString(cursor.getColumnIndex(dbAdapter.ANNOTATE));
				cursor.close();

				if (comparePhotoIsSet == true) {
					if (pHolder.isNotSet(pHolder)){
						pHolder.setPhoto(bitmap, 1);
						pHolder.setDate(date, 1);
					}
					else if (pHolder.isPartiallySet(pHolder)){
						pHolder.setPhoto(bitmap, 2);
						pHolder.setDate(date, 2);
						extractHolder(intent,pHolder);
					}	
				} else {

					Intent intent = new Intent(PhotoLayoutView.this, DisplayPhotoView.class);
					intent.putExtra("rowId", id);
					intent.putExtra("BitmapImage", bitmap);
					intent.putExtra("FolderName", folderName);
					intent.putExtra("TimeStamp", date);
					intent.putExtra("Tag", tag);
					intent.putExtra("Annotate", annotate);

					startActivity(intent);
				}
			}
		});
		
		/**
		 * When item is clicked in the GridView, it's id is recorded.
		 *  @return true
		 */
		gridView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {	
				entryID = id;
				
				showDialog(DIALOG_DELETE_PHOTO_ID);
				
				return true;
			}
		});
	}
	
	/**
	 * Recreate the database on start to prevent errors
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();

		dbHelper.open();
        fillData();
	}

	/**
	 * Close the database on stop to prevent errors
	 * 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		dbHelper.close();
		entriesCursor.close();
	}
	
	
	/**
	 * takeAPhoto launches a new camera activity.
	 */
    protected void takeAPhoto() {  	
    	File file = new File( _path );
    	 try {
             if( file.exists() == false) {
                 file.getParentFile().mkdirs();
                 file.createNewFile();
             }

         } catch (IOException e) {
             System.out.println("Could not create file.");
         }

        Uri outputFileUri = Uri.fromFile( file );

        Intent intentC = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intentC.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intentC, CAMERA_PHOTO_REQUEST );
    }
    
    protected void extractHolder(Intent i, PhotoHolder p){
    	if (p.isFullySet(p)){
    		i.putExtra("BitmapImage1", p.getPhoto(1));
    		i.putExtra("Date1", p.getDate(1));
    		i.putExtra("BitmapImage2", p.getPhoto(2));
    		i.putExtra("Date2", p.getDate(2));
    		p.clearPhotoHolder(p);
    		comparePhotoIsSet = false;
    		startActivity(i);
    	}
    	else{
    		//error
    	}
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
				String tag = "";
				String annotate= "";
				
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				//Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
				Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
				byte[] photo = baos.toByteArray();
				
				dbHelper.open();
				dbHelper.createPhotoEntry(date, folder, tag, annotate, photo);
				fillData();
				dbHelper.close();
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

	
	private void comparePhotoToast() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		
		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		image.setImageResource(R.drawable.info_notice);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText("Please select two photos to compare.\nPress Compare again to cancel.");
		
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
	private void cancelCompareToast() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		
		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		image.setImageResource(R.drawable.info_notice);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText("Photo Comparision Cancelled.");
		
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
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
			.setIcon(R.drawable.dialog_cancel)
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
