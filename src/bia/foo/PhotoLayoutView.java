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

public class PhotoLayoutView extends Activity
{
	private static final int CAMERA_PHOTO_REQUEST = 100;
	
	private Button newPhoto;
	private Button delButton;
	private TextView currentFolder;
	private GridView gridView;
	
	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
    
    public String folderName;
    
    static final int DIALOG_DELETE_PHOTO_ID = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogridview);
		
		newPhoto = (Button) findViewById(R.id.new_photo);
		delButton = (Button) findViewById(R.id.delete_photo);
        gridView = (GridView) findViewById(R.id.gridView1);
        currentFolder = (TextView) findViewById(R.id.current_folder);
        
		dbHelper = new dbAdapter(this);
        dbHelper.open();
        fillData();
        
        folderName = getIntent().getStringExtra("FolderName");
        currentFolder.setText(folderName);
        
		
		// When newButton is clicked, call takeAPhoto
		newPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{
				takeAPhoto();
			}
		});
		
		// When delButton is clicked delete the last selected photo
		delButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{
				showDialog(DIALOG_DELETE_PHOTO_ID);
    		}
		});
		
		// When item is clicked in the GridView, it's id is recorded
		gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				entryID = id;
			}
		});
		
		// When item is long clicked in the GridView it displays a larger image.
		gridView.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
			{
				ImageView imageView = (ImageView) v.findViewById(R.id.image1); 
				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				
				Intent intent = new Intent(PhotoLayoutView.this, DisplayPhotoView.class);
				intent.putExtra("BitmapImage", bitmap);
				startActivity(intent);
				
				return true;
			}
		});
	}
	
	
	/*
	 * Launches a new camera activity
	 */
    protected void takeAPhoto()
    {
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	startActivityForResult(intent, CAMERA_PHOTO_REQUEST);
    }
    
    
	/*
	 * If NewEntryView exited correctly, then put returned data into the database
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{	
		if(requestCode == CAMERA_PHOTO_REQUEST)
		{		
			if (resultCode == RESULT_OK)
			{
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
	
	/*
	 * Recreates the gridView and updates the list
	 */
	private void fillData()
	{
        // Get all of the notes from the database and create the item list
        entriesCursor = dbHelper.fetchAllPhotos();
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

					if(entryID != -1)
					{
						dbHelper.deletePhotoEntry(entryID);
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