package cmpt301.assign1;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

 

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