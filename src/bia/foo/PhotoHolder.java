package bia.foo;

import android.graphics.Bitmap;

/** 
 * Photo and Date Model
 * This holds the photo and string data for the photo layout view
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

	private Bitmap Photo1 = null;
	private String photoDate1 = null;
	private Bitmap Photo2 = null;
	private String photoDate2 = null;

	//checks if the photo holder is fully set for a photo comparision
	public boolean isFullySet(PhotoHolder holder){
		if(Photo1 != null && photoDate1 != null && Photo2 != null && photoDate2 != null){
			return true;
		}
		else{
			return false;
		}	
	}
	
	//checks if the photo holder is partially set for a single photo view
	public boolean isPartiallySet(PhotoHolder holder){
		if(Photo1 != null && photoDate1 != null && Photo2 == null && photoDate2 == null){
			return true;
		}
		else{
			return false;
		}
	}
	
	//clears the set photos
	public void clearPhotoHolder(PhotoHolder holder){
		Photo1 = null;
		photoDate1 = null;
		Photo2 = null;
		photoDate2 = null;
	}
	
	//gets bitmap from photo holder either bitmap 1 or 2
	public Bitmap getPhoto(int photoid){
		if(photoid == 1){
			return Photo1;
		}
		else if (photoid == 2){
			return Photo2;
		}
		else{
			return null;
		}
	}
	
	//gets string from photo holder either bitmap 1 or 2
	public String getDate(int dateid){
		if(dateid == 1){
			return photoDate1;
		}
		else if (dateid == 2){
			return photoDate2;
		}
		else{
			return null;
		}
	}
	
	
}
