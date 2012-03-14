package bia.foo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

//this is the initial view which shows groups of photos
public class MoleFinderActivity extends Activity {
	private static final String FILENAME = "file.sav";
	
	private Button addFolderButton;
	private Button deleteFolderButton;
	//@SuppressWarnings("unused")
	private String folderName;
	@SuppressWarnings("unused")
	private EditText input;
	private ListView list;

	static final int DIALOG_NEW_FOLDER_ID = 0;
	static final int DIALOG_DELETE_FOLDER_ID = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    //    input = new EditText(MoleFinderActivity.this);
        addFolderButton = (Button) findViewById(R.id.add_group);
        deleteFolderButton = (Button) findViewById(R.id.delete_group);
        list = (ListView) findViewById(R.id.photo_grouping_list);
//        //creation of the dialog box confirming item deletion
//        final AlertDialog.Builder addDialog = new AlertDialog.Builder(MoleFinderActivity.this);
//        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MoleFinderActivity.this);
//        // Setting Dialog Title
//        addDialog.setTitle("Adding a new folder...");
//        deleteDialog.setTitle("Confirm Delete...");
//        // Setting Dialog Message
//        addDialog.setMessage("Please specify the folder name to add.");
//        deleteDialog.setMessage("Are you sure you want to delete this group?");
//        // Setting the view the new folder dialog will use
//        addDialog.setView(input);
//
//
//        // Setting Positive "Yes" Button
//        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//        	public void onClick(DialogInterface dialog,int which) {
//        		//actions to complete when clicking yes
//        		dialog.cancel();
//        	}
//        });
//
//        // Setting Negative "NO" Button
//        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//        	public void onClick(DialogInterface dialog, int which) {
//        		//actions to complete when clicking cancel
//        		dialog.cancel();
//        	}
//        });
//
//
		// When item is clicked in the GridView, it's id is recorded
		list.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
			{
				String currentFolder = parent.getItemAtPosition(position).toString();
				
				Intent intent = new Intent(v.getContext(), PhotoLayoutView.class);
				intent.putExtra("FolderName", currentFolder);
				startActivity(intent);
				
				return true;
			}
		});
        
        
        addFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		if () {
//        			// brings up the dialog box to confirm deletion
        			showDialog(DIALOG_NEW_FOLDER_ID);
//        		} else {
//        			// if no item has been selected from the list
//        		}
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
        
        String[] foldersList = loadFromFile();
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(MoleFinderActivity.this, android.R.layout.simple_list_item_1, foldersList);
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
    				saveInFile(folderName);
    				
    				String[] foldersList = loadFromFile();
    		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(MoleFinderActivity.this, android.R.layout.simple_list_item_1, foldersList);
    		    	list.setAdapter(adapter);
    		    	
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
    		//dialog.show();
    	case DIALOG_DELETE_FOLDER_ID:
    		Builder deleteDialog = new AlertDialog.Builder(this);

    		deleteDialog.setTitle("Confirm Delete...")
    		.setMessage("Are you sure you want to delete this group?")
    		// do the work to define the addDialog
    		// Setting Positive "Yes" Button
    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking yes

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
        	//deleteDialog.show();
        }
        return null;
    }
    
    private void saveInFile(String text) {
    	try{
    		FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
    		fos.write(new String(text + "\n").getBytes());
    		fos.close();
    	} catch(FileNotFoundException e) {
    		e.printStackTrace();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    private String[] loadFromFile() {
    	ArrayList<String> folders = new ArrayList<String>();
    	try {
    		FileInputStream fis = openFileInput(FILENAME);
    		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    		String line = br.readLine();
    		while(line != null) {
    			folders.add(line);
    			line = br.readLine();
    		}
    		
    		fis.close();
    	} catch(FileNotFoundException e) {
    		e.printStackTrace();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return folders.toArray(new String[folders.size()]);
    }
}