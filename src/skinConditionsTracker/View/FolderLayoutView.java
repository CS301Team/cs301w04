package skinConditionsTracker.View;

import skinConditionsTracker.Controller.DatabaseAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
 * FolderLayoutView<br>
 * This is the initial view which shows folders of photos.
 * This activity starts PhotoLayoutView when a folder is clicked.
 * This activity passes the folder name to PhotoLayoutView.
 * 
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1<br><br>
 * 
 * April 06, 2012
 * 
 */

public class FolderLayoutView extends Activity {

	private Button addFolderButton;
	private String folderName;
	private ListView list;

	private long entryID = -1;
	private DatabaseAdapter dbHelper;

	private Cursor entriesCursor;

	/** OnCreate method for the FolderLayoutView.
	 * It initializes values for views and sets on 
	 * click listeners for all buttons in the view. 
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addFolderButton = (Button) findViewById(R.id.add_group);
		list = (ListView) findViewById(R.id.photo_grouping_list);

		dbHelper = new DatabaseAdapter(this);

		final Dialog deleteFolderDialog = deleteFolderDialog(); 
		final Dialog addFolderDialog = addFolderDialog();
		
		/** OnItemClickListener for the listview. When an item is clicked in the
		 * list it moves to the PhotoLayoutView and passes the folder name
		 * that was clicked. 
		 * */
		list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				String currentFolder = ((TextView)v.findViewById(R.id.folder_name)).getText().toString();
				
				Intent intent = new Intent(v.getContext(), PhotoLayoutView.class);
				intent.putExtra("FolderName", currentFolder);
				
				startActivity(intent);
			}
		});

		/** OnItemLongClickListener for the listview. When clicked it gets
		 * the entryID for the item clicked which is used later in deletion. 
		 * @see deleteFolderDialog()
		 * @return true*/
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				folderName = ((TextView)v.findViewById(R.id.folder_name)).getText().toString();
				entryID = id;
				
				deleteFolderDialog.show();

				return true;
			}
		});

		/** OnClickListener for the add folder button. When its pressed
		 * a dialog is shown which asks for input of a folder name 
		 * @see addFolderDialog()
		 * */
		addFolderButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addFolderDialog.show();
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
	 * Close the database and cursors on stop to prevent errors
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
		String[] from = new String[] { DatabaseAdapter.FOLDER };
		int[] to = new int[] { R.id.folder_name };

		// Create an array adapter and set it to display
		SimpleCursorAdapter adapter =
			new SimpleCursorAdapter(this, R.layout.folder_entry, entriesCursor, from, to);

		list.setAdapter(adapter);
	}

	/**
	 * Creates the add folder dialog when called.
	 * When it is shown a dialog appears asking the
	 * user to input a folder name. They can then confirm or cancel.
	 * The function returns the created dialog.
	 * 
	 * @return addDialog.create()
	 */
	private Dialog addFolderDialog() {
		final EditText input = new EditText(FolderLayoutView.this);

		Builder addDialog = new AlertDialog.Builder(FolderLayoutView.this);
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

				ToastCreator toast = new ToastCreator(FolderLayoutView.this);
				
				if(folderName.equals("")){
					toast.toaster("Please insert text for folder name.");
				}
				else if (folderName.contains("\n")){
					toast.toaster("Please make folder name one line.");
				}
				else if (folderName.contains("'")){
					toast.toaster("Please do not include single quotes.");
				}
				else if (folderName.length() > 50){
					toast.toaster("Please make the folder name shorter.");
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

	/**
	 * Creates the delete folder dialog when called.
	 * When it is shown a dialog appears asking the
	 * user to confirm deletion. They can then either confirm or cancel.
	 * The function returns the created dialog.
	 * 
	 * @return deleteDialog.create()
	 */
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
					dbHelper.deletePhotosInFolder(folderName);
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
}