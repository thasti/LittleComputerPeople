package com.example.Demo_Subj;

/**
 * Created by johannes on 22.11.2014.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.View;


public class GraphicalOutput extends View {

    private Subject subject;
    private Room room1;
    private Room room2;
    private Object object;

    public GraphicalOutput (Context c, Subject subject1, Room num1, Room num2, Object object1) {
        super(c);
        //setzt den Hintergrund

        subject = subject1;
        room1 = num1;
        room2 = num2;
        object = object1;

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(subject.getSubjBitmap(), 140, 350, false);

        if (subject.getAktRoomID() == 1) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(room1.getBitmapRoom(), canvas.getWidth(), canvas.getHeight(), false),0,0,iconPaint);
        }

        if (subject.getAktRoomID() == 2) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(room2.getBitmapRoom(), canvas.getWidth(), canvas.getHeight(), false),0,0,iconPaint);
        }

        if(subject.getInitialized() == 0){
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
