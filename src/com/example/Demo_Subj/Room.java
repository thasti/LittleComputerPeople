package com.example.Demo_Subj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johannes on 24.11.2014.
 */
public class Room {

    private Bitmap bitmapRoom;
    private int roomID;

    private List<Object> objectList;

    public Room (Bitmap bitmapRoomR, int ID, Context ctx) {
        bitmapRoom = bitmapRoomR;
        roomID = ID;
        objectList = new ArrayList<Object>();

        Resources resources = ctx.getResources();

        // populate this object list from XML information instead
        if (ID == 2) {
            objectList.add(new Object(BitmapFactory.decodeResource(resources, R.drawable.pflanze), 180, 40));
        }
    }

    public int getRoomID() {
        return roomID;
    }

    public Bitmap getBitmapRoom(){
        return bitmapRoom;
    }

    public List<Object> getObjectList() {
        return objectList;
    }
}
