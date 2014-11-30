package com.example.Demo_Subj;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private GraphicalOutput grafik;
    private Subject subject;
    private List<Room> roomList;
    private List<Bitmap> bitmapWalkingList;
    private int currentRoom;
    private int screenWidth;
    private int screenHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.main);

        Resources resources = getResources();

        //gets the size of the Display
        Display display = getWindowManager().getDefaultDisplay();
        /*
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        */
        // workaround for my older API 10 smartphone
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();

        Bitmap subjectBStand = BitmapFactory.decodeResource(resources, R.drawable.subjekt);
        bitmapWalkingList = new ArrayList<Bitmap>();
        bitmapWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_1));
        bitmapWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_2));
        bitmapWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_3));
        bitmapWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_4));
        subject = new Subject (subjectBStand, bitmapWalkingList,screenWidth, MyActivity.this);

        roomList = new ArrayList<Room>();
        // TODO: populate this list via the XML
        roomList.add(new Room(BitmapFactory.decodeResource(resources, R.drawable.wohnzimmer), 1, this));
        roomList.add(new Room(BitmapFactory.decodeResource(resources, R.drawable.schlafzimmer), 2, this));

        currentRoom = subject.getAktRoomID();

        grafik = new GraphicalOutput(this, subject, roomList);

        FrameLayout fl = (FrameLayout) findViewById(R.id.framelayout0);
        fl.addView(grafik);

        grafik.invalidate();

        Thread move = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // tick everything
                    subject.tick();
                    // TODO: find a good way to encapsule current room (world object would be good..)
                    // i dislike the way it is in Subject only, hmh
                    // it should be possible to display rooms where the subject is not at the moment
                    currentRoom = subject.getAktRoomID();
                    grafik.postInvalidate();
                    // TODO: instead of sleeping, pause the main and wait for signal from Tick-Object (this thread)
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
            }
        });
        move.start();
    }
}