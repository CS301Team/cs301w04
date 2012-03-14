package cmpt301.assign1;

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

public class CameraDatabaseTest extends Activity
{
	private static final int CAMERA_PHOTO_REQUEST = 100;
	
	private Button newPhoto;
	private Button delButton;
	private TextView currentFolder;
	private GridView gridView;
	
	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
    
    public String folderName = "wheee";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		newPhoto = (Button) findViewById(R.id.new_photo);
		delButton = (Button) findViewById(R.id.delete);
        gridView = (GridView) findViewById(R.id.grid_view);
        currentFolder = (TextView) findViewById(R.id.current_folder);
        
		dbHelper = new dbAdapter(this);
        dbHelper.open();
        fillData();
        
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
					dbHelper.deleteEntry(entryID);
					fillData();
				}
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
				
				dbHelper.createEntry(date, folder, photo);
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
        entriesCursor = dbHelper.fetchAllEntries();
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
	
	/*
	 * Records the id of the item in ListView that was clicked
	 * Used to delete the last selected item
	 * (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id)
//	{
//	    super.onListItemClick(l, v, position, id);
//	    entryID = id;
//	}
	
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