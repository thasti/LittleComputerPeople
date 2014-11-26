package com.example.Demo_Subj;

import android.graphics.Bitmap;

/**
 * Created by johannes on 24.11.2014.
 */
public class Room {

    private Bitmap bitmapRoom;
    private int roomID;

    public Room (Bitmap bitmapRoomR, int ID){
        bitmapRoom = bitmapRoomR;
        roomID = ID;
    }

    public int getRoomID() {
        return roomID;
    }

    public Bitmap getBitmapRoom(){
        return bitmapRoom;
    }

}
