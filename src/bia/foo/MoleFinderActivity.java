package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//this is the initial view which shows groups of photos
public class MoleFinderActivity extends Activity {
	private Button addFolderButton;
	private Button deleteFolderButton;
	private Editable folderName;
	private EditText input;
	
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
    

        
    }    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case DIALOG_NEW_FOLDER_ID:
    		input = new EditText(MoleFinderActivity.this);
    		Builder addDialog = new AlertDialog.Builder(MoleFinderActivity.this);
    		addDialog.setView(input);
    		addDialog.setTitle("Adding a new folder...")
    		.setMessage("Please specify the folder name to add.")
    		// do the work to define the addDialog
    		// Setting Positive "Yes" Button
    		.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog,int which) {
    				//actions to complete when clicking yes
    				folderName = input.getText();
    				dialog.cancel();
    				
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
    				dialog.cancel();
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

    				dialog.cancel();
    			}
    		})

            // Setting Negative "NO" Button
            	.setNegativeButton("No", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int which) {
            		//actions to complete when clicking cancel
            		dialog.cancel();
            	}
            });
        	return deleteDialog.create();
        	//deleteDialog.show();
        }
        return null;
    }
}