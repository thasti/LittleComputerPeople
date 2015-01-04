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
    private Resources resources;

    private Subject subject;
    private Room drawRoom;
    private Bitmap drawRoomB;
    private int lastRoom = -1;                          //Auf einen für die Räume ungültigen Wert vorinitialisieren
    private int aktRoomID = 1;

    private List<Bitmap> subjWalkForward;
    private List<Bitmap> subjWalkBackward;
    private Bitmap subjStand;
    private int direction;
    private float subjXPos;
    private float subjYPos;

    private int holdAnimCycles = 15;
    private int animCycle = 0;
    private int drawIndex = 0;

    private List<Integer> itemListWohn;                 //Existiert temporär, bis der XML-Parser eingebunden wurde
    private List<Integer> itemListSchlaf;               //Existiert temporär, bis der XML-Parser eingebunden wurde

    public RoomView(Context c) {
        super(c);

        this.ctx = c;
        this.p = null;

        resources = getResources();

        subject = GlobalInformation.getSubject();
        initSubject();
        fillItemList();
        fillRoomList();

        GlobalInformation.setCurrentRoom(aktRoomID);
    }

    private void initSubject(){

        List<Integer> subjList = subject.getPictureWalkID();
        subjWalkForward = new ArrayList<Bitmap>();
        subjWalkBackward = new ArrayList<Bitmap>();

        for(int i = 0; i < subjList.size(); i++){
            subjWalkBackward.add(Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(resources, subjList.get(i)),
                    GlobalInformation.getScreenWidth()/5,
                    (int)(GlobalInformation.getScreenHeight()/1.5),
                    false
            ));
        }
        for(int j = 0; j < subjWalkBackward.size(); j++){
            subjWalkForward.add(mirrorBitmap(subjWalkBackward.get(j)));
        }
        subjStand = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, subject.getPictureStandID()),
                GlobalInformation.getScreenWidth()/5,
                (int)(GlobalInformation.getScreenHeight()/1.5),
                false);
    }

    private Bitmap mirrorBitmap(Bitmap b){
        Matrix matrixMirror = new Matrix();
        matrixMirror.preScale(-1.0f, 1.0f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrixMirror, false);
        return b;
    }


    private void fillRoomList(){                                                                //Existiert temporär, bis der XML-Parser eingebunden wurde
        World.setRoom(1, new Room(1, R.drawable.schlafzimmer, 0.0, 0.0, itemListSchlaf, this.ctx));
        World.setRoom(2, new Room(2, R.drawable.wohnzimmer, 0.0, 0.0, itemListWohn, this.ctx));
    }

    private void fillItemList(){                                                                //Existiert temporär, bis der XML-Parser eingebunden wurde
        itemListWohn = new ArrayList<Integer>();
        World.setItem(1, new Item(1, R.drawable.pflanze, GlobalInformation.getScreenWidth()*0.5, GlobalInformation.getScreenHeight()*0.5, null, null, null, null, null, ctx));
        itemListWohn.add(World.getItemById(1).getID());
        World.setItem(2, new Item(2, R.drawable.boddle, GlobalInformation.getScreenWidth()*0.3, GlobalInformation.getScreenHeight()*0.5, null, null, null, null, null, ctx));
        itemListWohn.add(World.getItemById(2).getID());

        itemListSchlaf = new ArrayList<Integer>();
        World.setItem(3, new Item(3, R.drawable.boddle, GlobalInformation.getScreenWidth() * 0.3, GlobalInformation.getScreenHeight() * 0.5, null, null, null, null, null, ctx));
        itemListSchlaf.add(World.getItemById(3).getID());
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
        for(int i = 0; i < drawRoom.getContainingitems().size(); i++){
            itemId = drawRoom.getContainingitems().get(i);
            canvas.drawBitmap(
                    BitmapFactory.decodeResource(resources, World.getItemById(itemId).getPicresource()),
                    World.getItemById(itemId).getXPos().floatValue(),
                    World.getItemById(itemId).getYPos().floatValue(),
                    p
            );
        }

        getSubjectMovement();

        switch(direction){
            case 0:
                drawIndex = 0;
                animCycle = 0;
                canvas.drawBitmap(this.subjStand,
                        this.subjXPos,
                        this.subjYPos, this.p
                );
                break;

            case 1:
                animation();
                canvas.drawBitmap(this.subjWalkForward.get(drawIndex),
                        this.subjXPos,
                        this.subjYPos, this.p
                );
                break;

            case -1:
                animation();
                canvas.drawBitmap(this.subjWalkBackward.get(drawIndex),
                        this.subjXPos,
                        this.subjYPos, this.p
                );
                break;

            default:
                break;
        }
    }

    private void getSubjectMovement(){
        direction = subject.getDirection();
        subjXPos = subject.getXPos();
        subjYPos = subject.getYPos();
    }

    private void animation(){
        animCycle++;
        if(animCycle == holdAnimCycles){
            animCycle = 0;
            drawIndex++;
            if(drawIndex == subjWalkForward.size()) drawIndex = 0;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int eventAction = event.getAction();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Traverse all Objects in the current room

                int itemId;
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
