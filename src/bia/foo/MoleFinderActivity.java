package bia.foo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//this is the initial view which shows groups of photos
public class MoleFinderActivity extends Activity {
	private Button addFolderButton;
	private Button deleteFolderButton;
	@SuppressWarnings("unused")
	private Editable folderName;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText input = new EditText(MoleFinderActivity.this);
        addFolderButton = (Button) findViewById(R.id.add_group);
        deleteFolderButton = (Button) findViewById(R.id.delete_group);
        
        //creation of the dialog box confirming item deletion
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(MoleFinderActivity.this);
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MoleFinderActivity.this);
        // Setting Dialog Title
        addDialog.setTitle("Adding a new folder...");
        deleteDialog.setTitle("Confirm Delete...");
        // Setting Dialog Message
        addDialog.setMessage("Please specify the folder name to add.");
        deleteDialog.setMessage("Are you sure you want to delete this group?");
        // Setting the view the new folder dialog will use
        addDialog.setView(input);
        
     // Setting Positive "Yes" Button
        addDialog.setPositiveButton("Add folder", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int which) {
        		//actions to complete when clicking yes
        		folderName = input.getText();
        	}
        });

        // Setting Negative "NO" Button
        addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		//actions to complete when clicking cancel
        		dialog.cancel();
        	}
        });

        addFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		if () {
//        			// brings up the dialog box to confirm deletion
        			addDialog.show();
//        		} else {
//        			// if no item has been selected from the list
//        		}
        	}
        });
        
        // Setting Positive "Yes" Button
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int which) {
        		//actions to complete when clicking yes
        	}
        });

        // Setting Negative "NO" Button
        deleteDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int which) {
        		//actions to complete when clicking cancel
        		dialog.cancel();
        	}
        });

        deleteFolderButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		if () {
//        			// brings up the dialog box to confirm deletion
        			deleteDialog.show();
//        		} else {
//        			// if no item has been selected from the list
//        		}
        	}
        });

        
    }    
}