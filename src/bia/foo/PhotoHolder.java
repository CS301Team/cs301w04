package bia.foo;

/** 
 * Photo and Date Model
 * This holds the rowid for the photo layout view
 * 
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1
 * 
 * Thursday, March 22, 2012
 * 
 */

public class PhotoHolder {

	private long firstPhotoID = -1;
	private long secondPhotoID = -1;

	//checks if the photo holder is fully set for a photo comparision
	public boolean isFullySet(PhotoHolder holder){
		if(firstPhotoID != -1 && secondPhotoID != -1){
			return true;
		}
		else{
			return false;
		}	
	}
	
	//checks if the photo holder is partially set for a single photo view
	public boolean isPartiallySet(PhotoHolder holder){
		if(firstPhotoID != -1  && secondPhotoID == -1){
			return true;
		}
		else{
			return false;
		}
	}
	
	//checks if the photo holder is partially set for a single photo view
	public boolean isNotSet(PhotoHolder holder){
		if(firstPhotoID == -1 && secondPhotoID == -1){
			return true;
		}
		else{
			return false;
		}
	}
	
	//clears the set photos
	public void clearPhotoHolder(PhotoHolder holder){
		firstPhotoID = -1;
		secondPhotoID = -1;

	}
	
	//gets rowID from photo holder
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
	
	
	public void setPhoto(long rowID, int photoID){
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
