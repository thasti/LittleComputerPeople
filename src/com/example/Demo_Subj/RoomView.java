package com.example.Demo_Subj;

/**
 * 22.11.2014 - File created - JS
 * 29.11.2014 - taken over (refactored) - SB
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RoomView extends View {

    private Context ctx;
    private Paint p;
    private Subject subject;
    private Room drawRoom;
    private Bitmap drawRoomB;
    private int lastRoom = -1;          //Auf einen für die Räume ungültigen Wert vorinitialisieren

    private int aktRoomID = 1;

    private Resources resources;

    private List<Integer> itemListWohn;                 //Existiert temporär, bis der XML-Parser eingebunden wurde
    private List<Integer> itemListSchlaf;               //Existiert temporär, bis der XML-Parser eingebunden wurde

    boolean b = true;

    public RoomView(Context c) {
        super(c);

        this.ctx = c;
        this.p = null;

        resources = getResources();

        fillItemList();
        fillRoomList();

        GlobalInformation.setCurrentRoom(aktRoomID);

        subject = GlobalInformation.getSubject();
    }

    private void fillRoomList(){                                                                //Existiert temporär, bis der XML-Parser eingebunden wurde
        World.setRoom(1, new Room(1, R.drawable.schlafzimmer, 0.0, 0.0, itemListSchlaf, this.ctx));
        World.setRoom(2, new Room(2, R.drawable.wohnzimmer, 0.0, 0.0, itemListWohn, this.ctx));
    }

    private void fillItemList(){                                                                //Existiert temporär, bis der XML-Parser eingebunden wurde
        itemListWohn = new ArrayList<Integer>();
        World.setItem(1, new Item(1, R.drawable.pflanze, GlobalInformation.getScreenWidth()*0.5, GlobalInformation.getScreenHeight()*0.5, null, null, null, null, null, ctx));
        b &= itemListWohn.add(World.getItemById(1).getID());
        World.setItem(2, new Item(2, R.drawable.boddle, GlobalInformation.getScreenWidth()*0.3, GlobalInformation.getScreenHeight()*0.5, null, null, null, null, null, ctx));
        b &= itemListWohn.add(World.getItemById(2).getID());

        itemListSchlaf = new ArrayList<Integer>();
        World.setItem(3, new Item(3, R.drawable.boddle, GlobalInformation.getScreenWidth()*0.3, GlobalInformation.getScreenHeight()*0.5, null, null, null, null, null, ctx));
        b &= itemListSchlaf.add(World.getItemById(3).getID());
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // find out which room to draw
        //TODO should be replaced by a proper find() in the list


        if(lastRoom != GlobalInformation.getCurrentRoom()){                                     //hat sich lastroom gegenüber dem letzten Aufruf geändert? wenn ja dann lade den aktuellen Raum
            drawRoom = World.getRoomById(GlobalInformation.getCurrentRoom());
            drawRoomB = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(resources, drawRoom.getPicResource()),
                    GlobalInformation.getScreenWidth(),
                    GlobalInformation.getScreenHeight(),
                    false
            );
            lastRoom = drawRoom.getID();
        }


        if (drawRoom == null) {
            // room not found
            drawRoom = World.getRoomById(1);
        }

        canvas.drawBitmap(drawRoomB, 0, 0, p);

        // draw all items (TODO: add layering)
        //Layer als zusätzliche Eigenschaft von Item benötigt, wird ein Offset zur Y-Koordinate von Objekten hinzufügen

        int itemId;
        if(b){
            for(int i = 0; i < drawRoom.getContainingitems().size(); i++){
                itemId = drawRoom.getContainingitems().get(i);
                canvas.drawBitmap(
                        BitmapFactory.decodeResource(resources, World.getItemById(itemId).getPicresource()),
                        World.getItemById(itemId).getXPos().floatValue(),
                        World.getItemById(itemId).getYPos().floatValue(),
                        p
                );
            }
        }

        canvas.drawBitmap(subject.getBitmap(),
                subject.getXPos(),
                subject.getYPos(), p);
    }



    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int eventAction = event.getAction();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Traverse all Objects in the current room
                int itemId;
                if(b){
                    for(int i = 0; i < drawRoom.getContainingitems().size(); i++){
                        itemId = drawRoom.getContainingitems().get(i);
                        RectF boundingBox = new RectF(World.getItemById(itemId).getXPos().floatValue(),
                                World.getItemById(itemId).getYPos().floatValue(),
                                World.getItemById(itemId).getXPos().floatValue() + BitmapFactory.decodeResource(resources, World.getItemById(itemId).getPicresource()).getWidth(),
                                World.getItemById(itemId).getYPos().floatValue() + BitmapFactory.decodeResource(resources, World.getItemById(itemId).getPicresource()).getHeight());
                        if (boundingBox.contains((int)event.getX(), (int)event.getY())) {
                            // TODO call the use() function of the item
                            Toast.makeText(getContext(), "Click on " + World.getItemById(itemId).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                Rect changeViewBox = new Rect(0,0, 50, 50);
                if (changeViewBox.contains((int)event.getX(), (int)event.getY())) {
                    Intent i = new Intent(ctx, HouseActivity.class);
                    ctx.startActivity(i);
                }
                break;
        }
        return true;
    }
}
