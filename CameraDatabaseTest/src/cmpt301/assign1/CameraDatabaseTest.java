package cmpt301.assign1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

public class CameraDatabaseTest extends Activity
{
	private static final int PICK_CONTACT = 0;
	
	private Button newButton;
	private Button delButton;
	private GridView gridView;
	
	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		newButton = (Button) findViewById(R.id.new_entry);
		delButton = (Button) findViewById(R.id.delete);
        gridView = (GridView) findViewById(R.id.grid_view);
		
		dbHelper = new dbAdapter(this);
        dbHelper.open();
        fillData();
		
		// When newButton is clicked, call newLogEntry
		newButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{
				newLogEntry();
			}
		});
		
		// When delButton is clicked delete the last selected entry
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
	 * Launches a new activity of NewEntryView
	 */
	private void newLogEntry()
	{
		Intent intent = new Intent(this, NewEntryView.class);
		startActivityForResult(intent, PICK_CONTACT);		
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
        String[] from = new String[] { dbAdapter.PHOTO, dbAdapter.GROUP, dbAdapter.DATE};
        int[] to = new int[] { R.id.image1, R.id.text1, R.id.text2 };
        
        // Create an array adapter and set it to display
        SimpleCursorAdapter entries =
        		new SimpleCursorAdapter(this, R.layout.entry_row, entriesCursor, from, to);                         
		
        entries.setViewBinder(new PhotoViewBinder());
        
        Log.w("NumRows", entries.getCount() + "");
        
        gridView.setAdapter(entries);
        
        
    }
	
	/*
	 * If NewEntryView exited correctly, then put returned data into the database
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
        if (resultCode == RESULT_OK)
        {
        	super.onActivityResult(requestCode, resultCode, intent);
        	Bundle send = intent.getExtras();
        	
        	String date = send.getString(dbAdapter.DATE);
        	String station = send.getString(dbAdapter.GROUP);
        	byte[] photo = send.getByteArray(dbAdapter.PHOTO);
        	
        	dbHelper.createEntry(date, station, photo);
        	fillData();
        }
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