package skinConditionsTracker.Model;


import skinConditionsTracker.Model.DatabaseAdapter;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;

public class DatabaseAdapterProduct {
	private DatabaseHelper mDbHelper;
	private Context mCtx;
	

    /**
     * Database creation SQL statement.
     */
    
	private static final String DATABASE_CREATE_PHOTOS = 
    	"create table photos (_id integer primary key autoincrement, "
        + "date TEXT NOT NULL, folder TEXT NOT NULL, tag TEXT NOT NULL," +
        		"annotate TEXT NOT NULL, photo BLOB NOT NULL);";
    
    private static final String DATABASE_CREATE_FOLDERS =
    	"CREATE TABLE folders (_id integer primary key autoincrement, "
        + "folder TEXT NOT NULL);";
    
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;
	
    private static final String NAME = "dbAdapter";
    
	 private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {
	            db.execSQL(DATABASE_CREATE_PHOTOS);
	            db.execSQL(DATABASE_CREATE_FOLDERS);
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(NAME, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS photos");
	            db.execSQL("DROP TABLE IF EXISTS folders");
	            onCreate(db);
	        }
	    }

	public DatabaseHelper getMDbHelper() {
		return mDbHelper;
	}

	public void setMDbHelper(DatabaseHelper mDbHelper) {
		this.mDbHelper = mDbHelper;
	}

	public Context getMCtx() {
		return mCtx;
	}

	public void setMCtx(Context mCtx) {
		this.mCtx = mCtx;
	}

	/**
	 * Open the entries database. If it cannot be opened, try to create a new instance of the database. If it cannot be created, throw an exception to signal the failure
	 * @return  this (self reference, allowing this to be chained in an initialization call)
	 * @throws SQLException  if the database could be neither opened or created
	 */
	public DatabaseAdapter open(DatabaseAdapter databaseAdapter)
			throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		databaseAdapter.setMDb(mDbHelper.getWritableDatabase());
		return databaseAdapter;
	}

	public void close() {
		mDbHelper.close();
	}
}