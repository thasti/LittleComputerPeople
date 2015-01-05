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

        if(containingitems == null)
            this.containingitems = new ArrayList<Integer>();
        else if(containingitems != null)
            this.containingitems = containingitems;

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

    public List<Integer> getAttachedRooms(){return attachedRooms;};


}
