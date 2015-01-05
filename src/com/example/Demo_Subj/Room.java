package com.example.Demo_Subj;

//TODO : exceptions,
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johannes on 24.11.2014.
 * Graphical adapations - 04.12.2014
 * Continued by Karsten Becker
 */
public class Room {


    private int roomID;
    private Integer picresource;
    private Double xPos;
    private Double yPos;
    List<Integer> containingitems;
    private Context context;
    private int lowerRoomID = -1;
    private int upperRoomID = -1;
    private int leftRoomID = -1;
    private int rightRoomID = -1;
    List<Integer> attachedRooms;

    private List<Item> itemList;

    public Room (Integer ID, Integer picresource, Double x, Double y, List<Integer> containingitems,  Context context) {
        /*bitmapRoom = Bitmap.createScaledBitmap(bitmapRoomR,
                GlobalInformation.getScreenWidth(),
                GlobalInformation.getScreenHeight(),
                false);
        */
        this.roomID = ID;
        this.picresource = picresource;
        xPos = x;
        yPos = y;

        roomID = ID;
        itemList = new ArrayList<Item>();
        attachedRooms = new ArrayList<Integer>();
        //TODO populate this object list from XML information instead

        /*
        //itemList = new ArrayList<Item>();

        //Resources resources = ctx.getResources();

        if (ID == 2) {
            //itemList.add(new Item(BitmapFactory.decodeResource(resources, R.drawable.pflanze),1, 180, 40));
        }
        */
    }
    // returns room-ID
    public Integer getID() {
        return this.roomID;
    }

    // returns int of drawable ressource
    public Integer getPicResource(){
        return this.picresource;
    }

    // returns x-position
    public Double getXPos(){
        return this.xPos;
    }

    // returns y-position
    public Double getYPos(){
        return this.yPos;
    }

    //returns a list of int with item ids
    public List<Integer> getContainingitems() {
        return containingitems;
    }

    public List<Integer> getAttachedRooms(){
        return attachedRooms;
    };

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
