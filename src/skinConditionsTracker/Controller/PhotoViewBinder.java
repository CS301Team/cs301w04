package skinConditionsTracker.Controller;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

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
 * PhotoViewBinder<br>
 * This class is an adapter used to display photos in gridView.
 * It is used by PhotoLayoutView to display photos.
 * To use setViewBinder of a SimpleCursorAdapter.
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1<br><br>
 * 
 * April 06, 2012
 * 
 */

public class PhotoViewBinder implements SimpleCursorAdapter.ViewBinder {
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		if(view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			byte[] img = cursor.getBlob(columnIndex);
			iv.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
			return true;
		}	
		return false;
	}
}