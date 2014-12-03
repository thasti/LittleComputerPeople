package com.example.Demo_Subj;

/**
 * Created by johannes on 22.11.2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class GraphicalOutput extends View {

    public static final int TOUCH_BOX_SIZE = 50;

    private Subject subject;
    private List<Room> roomList;
    private Item item;
    Room drawRoom;

    public GraphicalOutput (Context c, Subject subject1, List<Room> m_roomList) {
        super(c);
        //setzt den Hintergrund und so
        this.roomList = m_roomList;
        subject = subject1;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        for (Iterator<Room> iter = roomList.iterator(); iter.hasNext(); ) {
            Room room = iter.next();
            if (room.getRoomID() == subject.getAktRoomID()) {
                drawRoom = room;
            }
        }
        if (drawRoom == null) {
            // room not found
            drawRoom = roomList.get(0);
        }
        //TODO kann man auslagern in den Konstruktor? - JS
        Bitmap background = Bitmap.createScaledBitmap(drawRoom.getBitmapRoom(),
                canvas.getWidth(),
                canvas.getHeight(),
                false);
        canvas.drawBitmap(background, 0, 0, iconPaint);


        List<Item> drawItems = drawRoom.getItemList();

        for (Iterator<Item> iter = drawItems.iterator(); iter.hasNext(); ){
            Item obj = iter.next();
            canvas.drawBitmap(obj.getBitmapO(), obj.getxPos(), obj.getyPos(), iconPaint);
        }
        //TODO alle resize Vorgänge in den Konstruktor auslagern? - JS
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(subject.getSubjBitmap(), 170, 330, false);

        // TODO subject init should probably NOT be here
        if(subject.getInitialized() == 0) {
            float canvasx = (float) canvas.getWidth();
            float canvasy = (float) canvas.getHeight();
            float bitmapx = (float) resizedBitmap.getWidth();
            float bitmapy = (float) resizedBitmap.getHeight();
            float posX = ((canvasx/2) - (bitmapx / 2));
            float posY = (((canvasy/2) - (bitmapy / 2)) + (canvasy/15));

            subject.setDefaultKoords(posX,posY,1);
            subject.setInitialized();
        }

        if(subject.getSubjBitmap() != null){
            canvas.drawBitmap(resizedBitmap,
                    subject.getxPos(),
                    subject.getyPos(),
                    iconPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int eventAction = event.getAction();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Traverse all Objects in the current room
                List<Item> allObjs = drawRoom.getItemList();
                
                for (Iterator<Item> iter = allObjs.iterator(); iter.hasNext(); ) {
                    Item obj = iter.next();
                    Rect boundingBox = new Rect((int)obj.getxPos(),
                            (int)obj.getyPos(),
                            (int)obj.getxPos() + obj.getBitmapO().getWidth(),
                            (int)obj.getyPos() + obj.getBitmapO().getHeight());
                    if (boundingBox.contains((int)event.getX(), (int)event.getY())) {
                        // TODO call the use() function of the item
                        Toast.makeText(getContext(), "Click on " + obj.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return true;
    }
}
