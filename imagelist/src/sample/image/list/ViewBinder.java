
package sample.image.list;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;

public class ViewBinder implements SimpleCursorAdapter.ViewBinder {
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		
		if(view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			byte[] img = cursor.getBlob(columnIndex);
			iv.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
			return true;
		}

		if(view instanceof RatingBar) {
			RatingBar rb = (RatingBar) view;
			int kcal = cursor.getInt(cursor.getColumnIndex(DBhelper.KEY_KCAL));
			int vitc = cursor.getInt(cursor.getColumnIndex(DBhelper.KEY_VC));
			rb.setRating((float) (kcal * ((float) vitc/1000.0)));
			return true;
		}
		
		return false;
	}
}
