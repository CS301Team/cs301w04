package skinConditionsTracker.Controller;

import skinConditionsTracker.Model.DatabaseModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
 * DatabaseAdapter
 * A simple SQLite database helper class. Gives the abilities needed
 * by the main application to access photos and available folders.
 * 
 * 
 * Much of the code of this class is from the Notepad Tutorial on
 * the Android Developer website.
 * Found at:
 * http://developer.android.com/resources/tutorials/notepad/index.html
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Friday, March 16, 2012
 * 
 */

public class DatabaseAdapter
{
	public DatabaseModel databaseModel = new DatabaseModel();
	public static final String ID = "_id";
	public static final String DATE = "date";
    public static final String FOLDER = "folder";
    public static final String PHOTO = "photo";
    public static final String ANNOTATE = "annotate";
    public static final String TAG = "tag";
   
    private SQLiteDatabase mDb;

  //  private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_PHOTOS = "photos";
    private static final String DATABASE_TABLE_FOLDERS = "folders";
  //  private static final int DATABASE_VERSION = 2;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DatabaseAdapter(Context ctx) {
        databaseModel.setMCtx(ctx);
    }

    /**
     * Open the entries database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DatabaseAdapter open() throws SQLException {
		return databaseModel.open(this);
	}

    public void close() {
		databaseModel.close();
	}

    /**
     * Create a new entry using the information provided. If the entry is
     * successfully created return the new rowId for that entry, otherwise return
     * a -1 to indicate failure.
     * 
     * @param date the date (in yyyy-mm-dd format)
     * @param folder the folder the photo is in
     * @param tag the tag the photo is under
     * @param photo the photo in byte array format
     * @return rowId or -1 if failed
     */
    public long createPhotoEntry(String date, String folder, String tag, String annotate, byte[] photo) {
        ContentValues initialValues = new ContentValues();
       
        initialValues.put(DATE, date);
        initialValues.put(FOLDER, folder);
        initialValues.put(PHOTO, photo);
        initialValues.put(TAG, tag);
        initialValues.put(ANNOTATE, annotate);
        
        return mDb.insert(DATABASE_TABLE_PHOTOS, null, initialValues);
    }
    
    /**
     * Create a new entry using the information provided. If the entry is
     * successfully created return the new rowId for that entry, otherwise return
     * a -1 to indicate failure.
     * 
     * @param folder the folder
     * @return rowId or -1 if failed
     */
    public long createFolder(String folder) {
        ContentValues initialValues = new ContentValues();
       
        initialValues.put(FOLDER, folder);
        
        return mDb.insert(DATABASE_TABLE_FOLDERS, null, initialValues);
    }
       

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId id of entry to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePhoto(long rowId) {
        return mDb.delete(DATABASE_TABLE_PHOTOS, ID + "=" + rowId, null) > 0;
    }
    
    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId id of entry to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteFolder(long rowId) {
        return mDb.delete(DATABASE_TABLE_FOLDERS, ID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all folders in the table
     * 
     * @return Cursor over all folders
     */
    public Cursor fetchAllFolders() {
        return mDb.query(DATABASE_TABLE_FOLDERS, new String[] {ID, 
        		FOLDER}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all tags in the table
     * 
     * @return Cursor over all tags
     */
    public Cursor fetchUniqueTags() throws SQLException {
    	return mDb.query(DATABASE_TABLE_PHOTOS, new String[] {ID, 
        		TAG}, null, null, TAG, null, null, null);
    }
    
    /**
     * Returns a cursor that points to data with the requested tag
     * 
     * @param tag retrieve photos with given tag
     * @return Cursor that traverses photos with given tag
     */
    public Cursor fetchPhotosUnderTag(String tag) {
    	Cursor mCursor =
            mDb.query(true, DATABASE_TABLE_PHOTOS, new String[] {ID, DATE, FOLDER, 
            		TAG, ANNOTATE, PHOTO}, TAG + "='" + tag +"'", null, null, null, null, null);
    	
        return mCursor;
    }
    
    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * 
     * @param rowId id of entry to retrieve
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor fetchPhoto(long rowId) throws SQLException {
        Cursor mCursor =
            mDb.query(true, DATABASE_TABLE_PHOTOS, new String[] {ID, DATE, FOLDER, 
            		TAG, ANNOTATE, PHOTO}, ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Returns a Cursor that points to data with the requested folder name
     * 
     * @param folder retrieve photos with given folder name 
     * @return Cursor that traverses photos with given folder name
     */
    public Cursor fetchPhotosInFolder(String folder) {
    	Cursor mCursor = mDb.query(DATABASE_TABLE_PHOTOS, new String[] {ID, DATE, FOLDER, TAG, ANNOTATE, PHOTO},
    			FOLDER + "='" + folder + "'", null, null, null, null, null);
        
        return mCursor;
    }
    
    
    /**
     * @param annotate set annotation on photo with given id
     * @param rowId the id of the photo entry
     */
    public void addAnnotationToPhoto(String annotate, long rowId)
    {
    	ContentValues args = new ContentValues();
    	args.put(ANNOTATE, annotate);
    	
    	mDb.update(DATABASE_TABLE_PHOTOS, args, ID +"="+rowId, null); 	
    }
    
    public void addTagToPhoto(String tag, long rowId)
    {
    	ContentValues args = new ContentValues();
    	args.put(TAG, tag);
    	
    	mDb.update(DATABASE_TABLE_PHOTOS, args, ID +"="+rowId, null); 	
    }

	public void setMDb(SQLiteDatabase mDb) {
		this.mDb = mDb;
	}
    
}