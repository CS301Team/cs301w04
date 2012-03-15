package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/* 
 * this is the initial view which shows groups of photos.
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 */

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

public class MoleFinderActivity extends Activity {
	
	private Button addFolderButton;
	private Button deleteFolderButton;
	private String folderName;
	@SuppressWarnings("unused")
	private EditText input;
	private ListView list;

	static final int DIALOG_NEW_FOLDER_ID = 0;
	static final int DIALOG_DELETE_FOLDER_ID = 1;
	
	private long entryID = -1;
	private dbAdapter dbHelper;
    
    private Cursor entriesCursor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addFolderButton = (Button) findViewById(R.id.add_group);
        deleteFolderButton = (Button) findViewById(R.id.delete_group);
        list = (ListView) findViewById(R.id.photo_grouping_list);

        dbHelper = new dbAdapter(this);
        dbHelper.open();
        fillData();
		
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				entryID = id;
			}
		});
        
        //When item is clicked in the GridView, it's id is recorded
		list.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
			{
				//String currentFolder = parent.getItemAtPosition(position).getString(1);
				
				//Don't know if this is the correct way of doing it?
				Cursor folderCursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
				startManagingCursor(folderCursor);
				folderCursor.moveToPosition(position);
				String currentFolder = folderCursor.getString(1);
				
				Intent intent = new Intent(v.getContext(), PhotoLayoutView.class);
				intent.putExtra("FolderName", currentFolder);
				startActivity(intent);
				
				return true;
			}
		});
        
        
        addFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        			showDialog(DIALOG_NEW_FOLDER_ID);
        	}
        });
        
 
        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		if () {
//        			// brings up the dialog box to confirm deletion
        			showDialog(DIALOG_DELETE_FOLDER_ID);
//        		} else {
//        			// if no item has been selected from the list
//        		}
        	}
        });
    }    
    
    private void fillData()
	{
        // Get all of the notes from the database and create the item list
        entriesCursor = dbHelper.fetchAllFolders();
        startManagingCursor(entriesCursor);
        
        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] { dbAdapter.FOLDER };
        int[] to = new int[] { R.id.folder_name };
        
        // Create an array adapter and set it to display
        SimpleCursorAdapter adapter =
        		new SimpleCursorAdapter(this, R.layout.folder_entry, entriesCursor, from, to);
        Log.w("NumRows", adapter.getCount() + "");
        
        list.setAdapter(adapter);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case DIALOG_NEW_FOLDER_ID:
    		final EditText input = new EditText(MoleFinderActivity.this);

    		Builder addDialog = new AlertDialog.Builder(MoleFinderActivity.this);
    		addDialog.setView(input);
    		addDialog.setTitle("Adding a new folder...")
    		.setMessage("Please specify the folder name to add.")
    		// do the work to define the addDialog
    		// Setting Positive "Yes" Button
    		.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking yes
    				
    				folderName = input.getText().toString();
    		
    				dbHelper.createFolder(folderName);
    				fillData();
    				
    				dialog.dismiss();
    				input.setText("");
    				
    				//just used to test the folder name
    				Context context = getApplicationContext();
    				int duration = Toast.LENGTH_SHORT;
    				Toast toast = Toast.makeText(context, folderName, duration);
    				toast.show();
    			}
    		})

    		// Setting Negative "NO" Button
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				//actions to complete when clicking cancel
    				dialog.dismiss();
    			}
    		});
    		return addDialog.create();
    	case DIALOG_DELETE_FOLDER_ID:
    		Builder deleteDialog = new AlertDialog.Builder(this);

    		deleteDialog.setTitle("Confirm Delete...")
    		.setMessage("Are you sure you want to delete this group?")
    		// do the work to define the addDialog
    		// Setting Positive "Yes" Button
    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking yes
    				if(entryID != -1)
    				{
    					//only deletes from the folders table, we need
    					//to implement deletion of the values from the
    					//entries table as well
    					dbHelper.deleteFolder(entryID);
    					fillData();
    					//entryID = -1;
    				}
    				dialog.dismiss();
    			}
    		})

            // Setting Negative "NO" Button
            	.setNegativeButton("No", new DialogInterface.OnClickListener() {
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