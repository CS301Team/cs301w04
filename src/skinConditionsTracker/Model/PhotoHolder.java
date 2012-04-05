package skinConditionsTracker.Model;

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
 * rowID Model<br>
 * This holds the rowid for the photo layout view.
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1<br><br>
 * 
 * April 06, 2012
 * 
 */

public class PhotoHolder {

	private long firstPhotoID = -1;
	private long secondPhotoID = -1;

	/** checks if the photo holder is fully set for a photo comparison
	 * 
	 * @param holder
	 * @return true/false
	 */
	public boolean isFullySet(PhotoHolder holder){
		if(firstPhotoID != -1 && secondPhotoID != -1){
			return true;
		}
		else{
			return false;
		}	
	}
	
	/** checks if the photo holder is partially set for a single photo view
	 * 
	 * @param holder
	 * @return true/false
	 */
	public boolean isPartiallySet(PhotoHolder holder){
		if(firstPhotoID != -1  && secondPhotoID == -1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** checks if the photo holder is partially set for a single photo view
	 * 
	 * @param holder
	 * @return true/false
	 */
	public boolean isNotSet(PhotoHolder holder){
		if(firstPhotoID == -1 && secondPhotoID == -1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** clear the rowIDs from the photo holder
	 * 
	 * @param holder
	 */
	public void clearPhotoHolder(PhotoHolder holder){
		firstPhotoID = -1;
		secondPhotoID = -1;
	}
	
	/** gets rowIDs from photo holder
	 * 
	 * @param photoID
	 * @return firstPhotoID/secondPhotoID
	 */
	public long getPhotoID(int photoID){
		if(photoID == 1){
			return firstPhotoID;
		}
		else if (photoID == 2){
			return secondPhotoID;
		}
		else{
			return -1;
		}
	}
	
	/** sets rowIDs for photo holder
	 * 
	 * @param rowID is the photoID
	 */
	public void setPhotoID(long rowID, int photoID){
		if(photoID == 1){
			firstPhotoID = rowID;
		}
		else if (photoID == 2){
			secondPhotoID = rowID;
		}
		else{
			//error
		}
	}	
}
