package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
 * MoleFinderActivity
 * This is the initial view which shows folders of photos.
 * This activity starts PhotoLayoutView when a folder is clicked.
 * This activity passes the folder name to PhotoLayoutView.
 * 
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 */

public class MoleFinderActivity extends Activity {
	
	private Button addFolderButton;
	private String folderName;
	private ListView list;

	private long entryID = -1;
	private dbAdapter dbHelper;
	
	private Cursor entriesCursor;
    
    /** OnCreate method for the MoleFinderActivity.
     * It initializes values for views and sets on 
     * click listeners for all buttons in the view. 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addFolderButton = (Button) findViewById(R.id.add_group);
        list = (ListView) findViewById(R.id.photo_grouping_list);

        dbHelper = new dbAdapter(this);
        
        final Dialog deleteFolder = deleteFolderDialog(); 
        final Dialog addFolder = addFolderDialog();
		/** OnItemClickListener for the listview. When an item is clicked in the
         * list it moves to the PhotoLayoutView and passes the folder name
         * that was clicked. 
		 * */
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Cursor folderCursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
				startManagingCursor(folderCursor);
				folderCursor.moveToPosition(position);
				String currentFolder = folderCursor.getString(1);
				
				Intent intent = new Intent(v.getContext(), PhotoLayoutView.class);
				intent.putExtra("FolderName", currentFolder);
				folderCursor.close();
				startActivity(intent);
			}
		});
        
        /** OnItemLongClickListener for the listview. When clicked it gets
		 * the entryID for the item clicked which is used later in deletion. 
         * @return true*/
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				entryID = id;
				
				deleteFolder.show();
				
				return true;
			}
		});
        
        /** OnClickListener for the add folder button. When its pressed
         * a dialog is shown which asks for input of a folder name 
         * @see onCreateDialog
         * */
        addFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		addFolder.show();
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
    
    /** Function call to retrieve all the folder names from the database.
     * Utilizes the simple cursor adapter to bind it to the listview */
    private void fillData() {
        // Get all of the notes from the database and create the item list
        entriesCursor = dbHelper.fetchAllFolders();
        startManagingCursor(entriesCursor);
        
        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] { dbAdapter.FOLDER };
        int[] to = new int[] { R.id.folder_name };
        
        // Create an array adapter and set it to display
        SimpleCursorAdapter adapter =
        		new SimpleCursorAdapter(this, R.layout.folder_entry, entriesCursor, from, to);
        
        list.setAdapter(adapter);
    }
    
    /** OnCreateDialog method to create the dialogs when called.
     * Uses a switch case mechanism to decide which dialog to display.
     * @param int id (Used for the switch/case)
     * When the ID is new folder it builds the dialog with an edit text
     * to receive input for the folder name. The user can then either select
     * add folder to create this folder or cancel to return to the main activity.
     * When the ID is delete folder a confirmation dialog appears asking the
     * user to make their final decision. They can confirm the delete or cancel.
     * Both cases return the created dialogs.
     * @return dialog.create() */
    
    private Dialog addFolderDialog() {
    	final EditText input = new EditText(MoleFinderActivity.this);

		Builder addDialog = new AlertDialog.Builder(MoleFinderActivity.this);
		// do the work to define the addDialog
		addDialog.setView(input);
		addDialog.setTitle("Adding a new folder...");
		addDialog.setIcon(R.drawable.dialog_add);
		addDialog.setMessage("Please specify the folder name to add.");
		// Setting Positive "Add folder" Button
		addDialog.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				//actions to complete when clicking Add folder
				folderName = input.getText().toString();
				
				if(folderName.equals("")){
					toaster("Please insert text for folder name.");
				}
				else if (folderName.contains("\n")){
					toaster("Please make folder name one line.");
				}
				else if (folderName.contains("'")){
					toaster("Please do not include single quotes.");
				}
				else if (folderName.length() > 50){
					toaster("Please make the folder name shorter.");
				}
				else{
					dbHelper.createFolder(folderName);
					fillData();
				}
				input.setText("");
			}
		});

		// Setting Negative "NO" Button
		addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//actions to complete when clicking cancel
				input.setText("");
			}
		});
		
		return addDialog.create();	
    }
    
    private Dialog deleteFolderDialog() {
    	Builder deleteDialog = new AlertDialog.Builder(this);

		// do the work to define the deleteDialog
		deleteDialog.setTitle("Confirm Delete...");
		deleteDialog.setIcon(R.drawable.dialog_cancel);
		deleteDialog.setMessage("Are you sure you want to delete this group?");
		// Setting Positive "Yes" Button
		deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				//actions to complete when clicking yes
				if(entryID != -1) {
					//only deletes from the folders table, we need
					//to implement deletion of the values from the
					//entries table as well later
					dbHelper.deleteFolder(entryID);
					fillData();
				}
			}
		});

        // Setting Negative "NO" Button
        	deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		//actions to complete when clicking no
        	}
        });
    	return deleteDialog.create(); 	
    }
    
    private void toaster(String s) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		
		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		image.setImageResource(R.drawable.info_notice);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText(s);
		
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}