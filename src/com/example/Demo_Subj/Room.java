package com.example.Demo_Subj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johannes on 24.11.2014.
 * Graphical adapations - 04.12.2014
 */
public class Room {

    private Bitmap bitmapRoom;
    private int roomID;
    private int lowerRoomID = -1;
    private int upperRoomID = -1;
    private int leftRoomID = -1;
    private int rightRoomID = -1;
    List<Integer> attachedRooms;

    private List<Item> itemList;

    public Room (Bitmap bitmapRoomR, int ID, Context ctx) {
        bitmapRoom = Bitmap.createScaledBitmap(bitmapRoomR,
                GlobalInformation.getScreenWidth(),
                GlobalInformation.getScreenHeight(),
                false);

        roomID = ID;
        itemList = new ArrayList<Item>();
        attachedRooms = new ArrayList<Integer>();

        Resources resources = ctx.getResources();

        //TODO populate this object list from XML information instead
        if (ID == 1) {
            itemList.add(new Item(BitmapFactory.decodeResource(resources, R.drawable.pflanze), 200, 10));
        }
    }

    public int getRoomID() {
        return roomID;
    }

    public Bitmap getBitmapRoom(){
        return bitmapRoom;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public List<Integer> getAttachedRooms(){
        return attachedRooms;
    };

    public int getId(){return roomID;};

    public void setLowerRoomID(int ID){
        lowerRoomID = ID;
    }

    public void setUpperRoomID(int ID){
        upperRoomID = ID;
    }

    public void setLeftRoomID(int ID){
        leftRoomID = ID;
    }

    public void setRightRoomID(int ID){
        rightRoomID = ID;
    }

    public void setAttachedRooms(int leftRoom, int rightRoom, int upperRoom, int lowerRoom)
    {
        lowerRoomID = lowerRoom;
        upperRoomID = upperRoom;
        leftRoomID = leftRoom;
        rightRoomID = rightRoom;

        if (lowerRoomID != -1){
            attachedRooms.add(lowerRoomID);
        }
        if (upperRoomID != -1){
            attachedRooms.add(upperRoomID);
        }
        if (leftRoomID != -1){
            attachedRooms.add(leftRoomID);
        }
        if (rightRoomID != -1){
            attachedRooms.add(rightRoomID);
        }
    }

    public int getLowerRoomID(){
        return lowerRoomID;
    }

    public int getUpperRoomID(){
        return upperRoomID;
    }

    public int getLeftRoomID(){
        return leftRoomID;
    }

    public int getRightRoomID(){
        return rightRoomID;
    }

}
