package com.example.Demo_Subj;

/**
 * Created by johannes on 22.11.2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.Iterator;
import java.util.List;

public class GraphicalOutput extends View {

    private Subject subject;
    private List<Room> roomList;
    private Object object;

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
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(subject.getSubjBitmap(), 140, 350, false);

        Room drawRoom = null;
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

        Bitmap background = Bitmap.createScaledBitmap(drawRoom.getBitmapRoom(),
                canvas.getWidth(),
                canvas.getHeight(),
                false);
        canvas.drawBitmap(background, 0, 0, iconPaint);

        List<Object> drawObjects = drawRoom.getObjectList();

        for (Iterator<Object> iter = drawObjects.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            canvas.drawBitmap(obj.getBitmapO(), obj.getxPos(), obj.getyPos(), iconPaint);
        }

        // TODO subject init should probably NOT be here
        if(subject.getInitialized() == 0) {
            float canvasx = (float) canvas.getWidth();
            float canvasy = (float) canvas.getHeight();
            float bitmapx = (float) resizedBitmap.getWidth();
            float bitmapy = (float) resizedBitmap.getHeight();
            float posX = ((canvasx/2) - (bitmapx / 2));
            float posY = (((canvasy/2) - (bitmapy / 2)) + (canvasy/20));

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
}
