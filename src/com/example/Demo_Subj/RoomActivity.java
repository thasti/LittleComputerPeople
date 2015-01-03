package com.example.Demo_Subj;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JS - note on 29.11.2014.
 * edited by SB and JU
 */

public class RoomActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private RoomView grafik;

    //Variablen für Timer
    private Timer timer = new Timer();      //Tiemr wird global instanziiert, damit alle Methoden in dieser Datei Zugriff auf die richtige Instanz haben
    private int tick;

    //Variablen für das noch nicht implementierte Pausieren
    //private Object mPauseLock;
    //private boolean running = true;

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
        GlobalInformation.setScreenHeight(display.getHeight());
        GlobalInformation.setScreenWidth(display.getWidth());


        final Subject subject;
        subject = new Subject (this);
        GlobalInformation.setSubject(subject);


        grafik = new RoomView(this);

        FrameLayout fl = (FrameLayout) findViewById(R.id.framelayout0);
        fl.addView(grafik);

        grafik.invalidate();


        tick = InternalClock.getTick();

        InternalClock.init();

        //timer = new Timer();

        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                //subject.tick();
                //GlobalInformation.setCurrentRoom(grafik.getAktRoomID());
                subject.tick();
                grafik.postInvalidate();
                InternalClock.computeTime();
                //clock.computeTime();                                       //returns boolean Value
                //if(running){
                    //subject.tick();
                    //GlobalInformation.setCurrentRoom(subject.getAktRoomID());
                    //grafik.postInvalidate();
                    //InternalClock.computeTime();
                /*}
                else{                               //Eventuell wird der Timer zum pausieren später beendet (cancel in onPause)
                                                    //und neu erstellt (schedule in onResume())
                    synchronized (mPauseLock) {
                        while (!running) {
                            try {
                                mPauseLock.wait();              //Wenn ich das richtig verstehe, wird hier versucht, mPauseLock
                                                                //solange zu pausieren(xyz.wait() legt eine Thread schlafen bis
                                                                //er durch xyz.notify() geweckt wird), wie running false ist.
                                                                //Theoretisch sollte es ausreichen, den Thread einmal schlafen zu legen

                            } catch (InterruptedException e) {

                            }
                        }
                    }
                }*/
            }
        }, 0, tick);
    }
    /*
    @Override
    public void onPause() {
        super.onPause();
        synchronized (mPauseLock) {
            running = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        synchronized (mPauseLock) {
            running = true;
            mPauseLock.notifyAll();
        }
    }
    */
    @Override
    public void onBackPressed() {
        //Kills the app immediately
        //TODO figure out a way to do this more safely and elegant
        super.onBackPressed();
        timer.cancel();             //Timer sauber beenden
        this.finish();
        System.exit(0);
    }
}