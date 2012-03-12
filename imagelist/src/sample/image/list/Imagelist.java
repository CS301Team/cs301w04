
package sample.image.list;

import android.graphics.Bitmap;

public class Imagelist{
    private Bitmap bmp;
    private String name;
    private int kcal;
    private int vitaminc;

    public Imagelist(Bitmap b, String n, int k, int v) {
        bmp = b;
        name = n;
        kcal = k;
        vitaminc = v;
    }
   
    public Bitmap getBitmap() { return bmp; }
    public String getName() { return name; }
    public int getKcal() { return kcal; }
    public int getVitaminC() { return vitaminc; }
} 
