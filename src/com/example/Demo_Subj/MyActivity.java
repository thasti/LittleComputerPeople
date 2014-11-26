package com.example.Demo_Subj;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private GraphicalOutput grafik;
    private Subject subject;
    private Room wohnzimmer;
    private Room schlafzimmer;
    private Object pflanze;
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
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        Bitmap subjectB = BitmapFactory.decodeResource(resources, R.drawable.subjekt);
        subject = new Subject (subjectB, screenWidth, MyActivity.this);

        Bitmap wohnzimmerB = BitmapFactory.decodeResource(resources, R.drawable.wohnzimmer);
        wohnzimmer = new Room (wohnzimmerB, 1);

        Bitmap schlafzimmerB = BitmapFactory.decodeResource(resources, R.drawable.schlafzimmer);
        schlafzimmer= new Room (schlafzimmerB, 2);

        Bitmap pflanzeB = BitmapFactory.decodeResource(resources, R.drawable.pflanze);
        pflanze= new Object (pflanzeB, 10, 20);

        grafik = new GraphicalOutput(this, subject, wohnzimmer, schlafzimmer, pflanze);

        FrameLayout fl = (FrameLayout) findViewById(R.id.framelayout0);
        fl.addView(grafik);

        //grafik.invalidate();
        grafik.invalidate();

        Thread move = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    subject.setDest(680, 2);
                    while (subject.reachedDest() == 0) {
                        subject.getNextBitmap();
                        try {
                            Thread.sleep(10);
                            // Do some stuff
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                        grafik.postInvalidate();
                    }
                    try {
                        Thread.sleep(5000);
                        // Do some stuff
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                    subject.setDest(40, 1);
                    while (subject.reachedDest() == 0) {
                        subject.getNextBitmap();
                        try {
                            Thread.sleep(10);
                            // Do some stuff
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                        grafik.postInvalidate();
                    }
                    try {
                        Thread.sleep(5000);
                        // Do some stuff
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
            }
        });
        move.start();
    }
}