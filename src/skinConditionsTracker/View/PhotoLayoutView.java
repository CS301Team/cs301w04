package skinConditionsTracker.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import skinConditionsTracker.Controller.DatabaseAdapter;
import skinConditionsTracker.Controller.PhotoViewBinder;
import skinConditionsTracker.Model.PhotoHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
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
 * The folder name is given to this activity from FolderLayoutView.
 * This activity calls DisplayPhotoView, when a photo is clicked.
 * This activity passes a rowID which the DisplayPhotoView uses to 
 * populate its imageviews. The layout can also compare photos and
 * query photos by tags.
 * 
 * An issue we have with class is closing the cursor for the 
 * query tag dialog. After you use it you get a fill window error.
 * It doesn't effect the running of the program. We've tried closing
 * it multiple ways but it starts to crash our program so we've just
 * left it as is.
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * April 06, 2012
 * 
 */

public class PhotoLayoutView extends Activity
{
	private static final int CAMERA_PHOTO_REQUEST = 100;

	private Button newPhoto;
	private Button compPhoto;
	private Button queryByTag;
	private TextView currentFolder;
	private GridView gridView;

	private long entryID = -1;
	
	private DatabaseAdapter dbHelper;

	private Cursor entriesCursor;
	private Cursor tagCursor;
	
	private String folderName;
	
	private String _path;
	private PhotoHolder pHolder;

	private boolean comparePhotoIsSet = false;

	/** onCreate called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogridview);

		newPhoto = (Button) findViewById(R.id.new_photo);
		compPhoto = (Button) findViewById(R.id.compare_photo);
		queryByTag = (Button) findViewById(R.id.query_tag);
		gridView = (GridView) findViewById(R.id.gridView1);
		currentFolder = (TextView) findViewById(R.id.current_folder);
		
		final Intent intent = new Intent(PhotoLayoutView.this, ComparePhotoView.class);

		final Dialog deletePhotoDialog = deletePhotoDialog(); 
		
		_path = Environment.getExternalStorageDirectory() + "/images/temp_photo_holder.jpg";

		pHolder = new PhotoHolder();

		dbHelper = new DatabaseAdapter(this);

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


		/** 
		 * When comparePhoto button is clicked, tells the user
		 * to select two photos to compare or press compare button
		 * again to cancel the comparison.
		 */
		compPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastCreator toast = new ToastCreator(PhotoLayoutView.this);
				
				if (comparePhotoIsSet == false) {
					comparePhotoIsSet = true;
					toast.toaster("Please select two photos to compare.\nPress compare again to cancel.");
				}
				else if (comparePhotoIsSet == true) {
					comparePhotoIsSet = false;
					pHolder.clearPhotoHolder(pHolder);
					toast.toaster("Photo Comparison Cancelled.");
				}
			}
		});

		/** 
		 * This button makes a dialog appear that allows the user to
		 * query the photos by tag.
		 */
		queryByTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				Dialog queryTagDialog = queryTagDialog(); 
				queryTagDialog.show();
			}
		});

		/** 
		 *  When item is clicked in the GridView it gets the rowID of the image.
		 *  If in photo compare mode it sets the rowID in a holder to wait for a
		 *  second image to be clicked.
		 *  An activity to DisplayPhotoView or ComparePhotoView is then called.
		 */
		gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				if (comparePhotoIsSet == true) {
					if (pHolder.isNotSet(pHolder)){
						pHolder.setPhotoID(id, 1);
					}
					else if (pHolder.isPartiallySet(pHolder)) {
						pHolder.setPhotoID(id, 2);
						extractHolder(intent, pHolder);
					}	
				} else {
					Intent intent = new Intent(PhotoLayoutView.this, DisplayPhotoView.class);
					intent.putExtra("rowId", id);
					startActivity(intent);
				}
			}
		});

		/**
		 * When item is long clicked in the GridView, it's id is recorded
		 * and a dialog appears to allow deletion..
		 *  @return true
		 */
		gridView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {	
				entryID = id;

				deletePhotoDialog.show();
				return true;
			}
		});
	}

	/**
	 * Recreate the database on start and fill the gridview. 
	 */
	@Override
	protected void onStart() {
		super.onStart();

		dbHelper.open();
		fillData();
	}

	/**
	 * Close the database on stop to prevent errors. 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		entriesCursor.close();
		dbHelper.close();
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

			i.putExtra("RowIdOne", p.getPhotoID(1));
			i.putExtra("RowIdTwo", p.getPhotoID(2));

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
		String[] from = new String[] { DatabaseAdapter.PHOTO, DatabaseAdapter.DATE };
		int[] to = new int[] { R.id.image1, R.id.text1 };

		// Create an array adapter and set it to display
		SimpleCursorAdapter entries =
			new SimpleCursorAdapter(this, R.layout.entry_row, entriesCursor, from, to);                         

		entries.setViewBinder(new PhotoViewBinder());

		gridView.setAdapter(entries);
	}

	/** Method to create the deletePhotoDialog when called.
	 * When it is shown a confirmation dialog appears asking the
	 * user to make their final decision. They can confirm the delete or cancel.
	 * The function returns the created dialog.
	 * @return deleteDialog.create() */

	private Dialog deletePhotoDialog() {
		Builder deleteDialog = new AlertDialog.Builder(PhotoLayoutView.this);
		deleteDialog.setTitle("Confirm Delete...");
		deleteDialog.setIcon(R.drawable.dialog_cancel);
		deleteDialog.setMessage("Are you sure you want to delete this photo?");
		// do the work to define the deleteDialog
		// Setting Positive "Yes" Button
		deleteDialog.setPositiveButton("Delete Photo", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				//actions to complete when clicking yes

				if(entryID != -1) {
					dbHelper.deletePhoto(entryID);
					fillData();
				}
			}
		});

		// Setting Negative "NO" Button
		deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//actions to complete when clicking cancel
			}
		});
		return deleteDialog.create();
	}

	/** Method to create the queryTagDialog when called.
	 * When it is shown a spinner appears asking the
	 * user to select a tag. Once a tag is selected it populates the
	 * gridview with photos that have that tag.
	 * The function returns the created dialog.
	 * @return queryTagDialog.create() */
	
	private Dialog queryTagDialog() {
		Builder queryTagDialog = new AlertDialog.Builder(PhotoLayoutView.this);

		LayoutInflater inflater = LayoutInflater.from(PhotoLayoutView.this);
		final View layout = inflater.inflate(R.layout.tag_spinner, null);
		queryTagDialog.setView(layout);
		Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);

		queryTagDialog.setTitle("Tag selection...");
		queryTagDialog.setMessage("Select tag to view photos from that group.");
		queryTagDialog.setIcon(R.drawable.question_mark);
		// do the work to define the sortDialog
		// Setting Neutral Button
		queryTagDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				//actions to complete when clicking neutral
			}
		});

		tagCursor = dbHelper.fetchUniqueTags();
		startManagingCursor(tagCursor);

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

		tagCursor.close();
		
		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(PhotoLayoutView.this,
				android.R.layout.simple_spinner_item, tagList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected (AdapterView<?> parent, View v, int position, long id) {
				String selectedTag = ((TextView)v.findViewById(android.R.id.text1)).getText().toString();
				if (!selectedTag.equals(def)) {
					tagCursor = dbHelper.fetchPhotosUnderTag(selectedTag);

					// Create an array to specify the fields we want to display in the list
					String[] from = new String[] { DatabaseAdapter.PHOTO, DatabaseAdapter.DATE};
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

		return queryTagDialog.create();
	}
}
