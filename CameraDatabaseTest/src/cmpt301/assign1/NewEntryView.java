package cmpt301.assign1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/* 
 * This class is the secondary activity which takes user input
 * and returns it in a bundle once the save button is clicked.
 * 
 * @author Andrea Budac: abudac
 * 
 * Monday, February 6, 2012
 * 
 */

/**
 * Assignment 1: Fuel Consumption Tracking
 * Copyright (C) 2012 Andrea Budac
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

public class NewEntryView extends Activity 
{
	private EditText date;
	private EditText station;
	private Button saveButton;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_entry_view);
		setTitle(R.string.new_entry);
		
		date = (EditText) findViewById(R.id.date);
		station = (EditText) findViewById(R.id.station);
		
		saveButton = (Button) findViewById(R.id.save);
		
		// Retrieve Data from text boxes when button is clicked
		saveButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				Bundle bundle = new Bundle();

				bundle.putString(dbAdapter.DATE, date.getText().toString());
				bundle.putString(dbAdapter.STATION, station.getText().toString());
				
				// Send data back in a bundle
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}
}