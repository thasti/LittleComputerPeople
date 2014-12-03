package com.example.Demo_Subj;

import android.graphics.Bitmap;

/**
 * Created by johannes on 24.11.2014.
 */
public class Object {

    private Bitmap bitmapO;
    private float xPos;
    private float yPos;


    public Object (Bitmap bitmap, float x, float y){
        bitmapO = bitmap;
        xPos = x;
        yPos = y;
    }


    public float getxPos(){
        return xPos;
    }
    public float getyPos(){
        return yPos;
    }
    public Bitmap getBitmapO(){
        return bitmapO;
    }

}
