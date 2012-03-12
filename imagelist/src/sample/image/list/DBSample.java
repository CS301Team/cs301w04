
package sample.image.list;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class DBSample extends ListActivity {
    private DBhelper mDB;
       
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        mDB = new DBhelper(this);
       
        mDB.Reset(); 
        
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        
        mDB.createFruitEntry(new Imagelist(img, "Banane",   92, 10));
        mDB.createFruitEntry(new Imagelist(img, "Kiwi",     56, 71));
        mDB.createFruitEntry(new Imagelist(img, "Pfirsich", 41, 10));
        mDB.createFruitEntry(new Imagelist(img, "Zitrone",  40, 51));
       
        String[] columns = {mDB.KEY_ID, mDB.KEY_IMG, mDB.KEY_NAME, mDB.KEY_KCAL, mDB.KEY_VC};
        String   table   = mDB.FRUITS_TABLE;
        
        Cursor c = mDB.getHandle().query(table, columns, null, null, null, null, null);

        startManagingCursor(c);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
                R.layout.fruitlist, 
                c, 
                new String[] {mDB.KEY_IMG, mDB.KEY_NAME, mDB.KEY_KCAL, mDB.KEY_VC},
                new int[] {R.id.img, R.id.txt, R.id.rating});
        
        adapter.setViewBinder(new ViewBinder());

        setListAdapter(adapter);
    }
} 
