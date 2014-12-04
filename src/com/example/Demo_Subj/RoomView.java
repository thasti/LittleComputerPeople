package com.example.Demo_Subj;

/**
 * 22.11.2014 - File created - JS
 * 29.11.2014 - taken over (refactored) - SB
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class RoomView extends View {

    private Context ctx;
    private Paint p;
    private Subject subject;
    private List<Room> roomList;
    private Room drawRoom;

    public RoomView(Context c) {
        super(c);

        this.ctx = c;
        this.roomList = GlobalInformation.getRoomList();
        this.subject = GlobalInformation.getSubject();
        this.p = null;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // find out which room to draw
        //TODO should be replaced by a proper find() in the list
        for (Iterator<Room> iter = roomList.iterator(); iter.hasNext(); ) {
            Room room = iter.next();
            if (room.getRoomID() == GlobalInformation.getCurrentRoom()) {
                drawRoom = room;
            }
        }
        if (drawRoom == null) {
            // room not found
            drawRoom = roomList.get(0);
        }

        // draw the background image
        canvas.drawBitmap(drawRoom.getBitmapRoom(), 0, 0, p);

        // draw all items (TODO: add layering)
        List<Item> drawItems = drawRoom.getItemList();
        
        for (Iterator<Item> iter = drawItems.iterator(); iter.hasNext(); ){
            Item obj = iter.next();
            canvas.drawBitmap(obj.getBitmapO(), obj.getxPos(), obj.getyPos(), p);
        }

        // draw the subject
        canvas.drawBitmap(subject.getSubjBitmap(),
                subject.getxPos(),
                subject.getyPos(), p);
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
