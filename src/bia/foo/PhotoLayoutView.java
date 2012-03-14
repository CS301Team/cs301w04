package bia.foo;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
				if(entryID != -1)
				{
					dbHelper.deletePhotoEntry(entryID);
					fillData();
				}
    		}
		});
		
//		// When item is clicked in the GridView, it's id is recorded
//		gridView.setOnItemClickListener(new OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> l, View v, int position, long id)
//			{
//				entryID = id;
//			}
//		});
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
	
//	/*
//	 * Re-calculates the statistics using a cursor
//	 * (includes error checking if any data exists)
//	 * 
//	 */
//	private void calculateStats()
//	{
//		double totalFuelCost = 0;
//		double totalDist = 0;
//		double totalLitres = 0;
//		double fuelRate = 0;
//		
//		
//		// Set the stats to blank for no data case
//		totalCost.setText(R.string.total_cost_blank);
//		totalDistance.setText(R.string.basic_blank);
//		fuelConsume.setText(R.string.basic_blank);
//		
//		// Calculate the total fuel cost statistic
//		calcCursor = dbHelper.fetchColumn(dbAdapter.FUEL_COST);
//		// if there is any data pointed to by the cursor
//		if(calcCursor.moveToFirst())
//		{
//			totalFuelCost = totalFuelCost + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.FUEL_COST));
//			while(calcCursor.moveToNext())
//			{
//				totalFuelCost = totalFuelCost + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.FUEL_COST));
//			}
//			totalCost.setText(df2.format(totalFuelCost));
//		
//			// Calculate the total trip distance statistic
//			calcCursor = dbHelper.fetchColumn(dbAdapter.TRIP_DISTANCE);
//			totalDist = totalDist + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.TRIP_DISTANCE));
//			while(calcCursor.moveToNext())
//			{
//				totalDist = totalDist + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.TRIP_DISTANCE));
//			}
//			totalDistance.setText(df1.format(totalDist));
//			
//			// Calculate the total litres
//			calcCursor = dbHelper.fetchColumn(dbAdapter.FUEL_AMOUNT);
//			totalLitres = totalLitres + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.FUEL_AMOUNT));
//			while(calcCursor.moveToNext())
//			{
//				totalLitres = totalLitres + calcCursor.getDouble(calcCursor.getColumnIndex(dbAdapter.FUEL_AMOUNT));
//			}
//			
//			// Calculate the total fuel amount statistic
//			if(totalDist != 0)
//			{
//				totalDist = totalDist/100;
//				fuelRate = totalLitres/totalDist;
//				fuelConsume.setText(df1.format(fuelRate));
//			}
//		}
//	}
}















//package bia.foo;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
////this view is a grid of photos inside each group
//public class PhotoLayoutView extends Activity {
//	private Button deletePhotoButton;
//	
//	@Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.photogridview);
//        
//        deletePhotoButton = (Button) findViewById(R.id.delete_photo);
//        
//      //creation of the dialog box confirming item deletion
//        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(PhotoLayoutView.this);
//        // Setting Dialog Title
//        deleteDialog.setTitle("Confirm Delete...");
//        // Setting Dialog Message
//        deleteDialog.setMessage("Are you sure you want to delete this photo?");
//        
//     // Setting Positive "Yes" Button
//        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//        	public void onClick(DialogInterface dialog,int which) {
//        		//actions to complete when clicking yes
//        	}
//        });
//
//        // Setting Negative "NO" Button
//        deleteDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//        	public void onClick(DialogInterface dialog, int which) {
//        		//actions to complete when clicking cancel
//        		dialog.cancel();
//        	}
//        });
//
//        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
//        	public void onClick(View v) {
////        		if () {
////        			// brings up the dialog box to confirm deletion
//        			deleteDialog.show();
////        		} else {
////        			// if no item has been selected from the list
////        		}
//        	}
//        });
//    }
//}
