
package sample.image.list;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;

public class DBhelper {
    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_NAME = "name";
    public static final String KEY_KCAL = "kcal";
    public static final String KEY_VC = "vitaminc";
    public static final String KEY_IMG = "image";
   
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
   
    private static final String DATABASE_NAME = "FruitDB";
    private static final int DATABASE_VERSION = 1;
   
    public static final String FRUITS_TABLE = "fruits";

    private static final String CREATE_FRUITS_TABLE = "create table "+FRUITS_TABLE+" ("
                                         +KEY_ID+" integer primary key autoincrement, "
                                         +KEY_IMG+" blob not null, "
                                         +KEY_NAME+" text not null unique, "
                                         +KEY_KCAL+" integer not null, "
                                         +KEY_VC+" integer not null);";
                                             
    private final Context mCtx;
    private boolean opened = false;
    

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
   
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FRUITS_TABLE);
        }
   
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+FRUITS_TABLE);
            onCreate(db);
        }
    }
   
    public void Reset() {
    	openDB();
    	mDbHelper.onUpgrade(this.mDb, 1, 1);
    	closeDB();
    }
   
    public DBhelper(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }
   
    private SQLiteDatabase openDB() {
        if(!opened)
            mDb = mDbHelper.getWritableDatabase();
        opened = true;
        return mDb;
    }
    
    public SQLiteDatabase getHandle() { return openDB(); }
   
    private void closeDB() {
        if(opened)
            mDbHelper.close();
        opened = false;
    }

    public void createFruitEntry(Imagelist fruit) {
    	openDB();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        fruit.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMG, out.toByteArray());            
        cv.put(KEY_NAME, fruit.getName());
        cv.put(KEY_KCAL, fruit.getKcal());
        cv.put(KEY_VC, fruit.getVitaminC());
        mDb.insert(FRUITS_TABLE, null, cv);
        closeDB();
    }
} 
