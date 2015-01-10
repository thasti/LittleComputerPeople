package com.example.Demo_Subj;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

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
    private boolean paused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.main);

        Resources resources = getResources();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //gets the size of the Display
        Display display = getWindowManager().getDefaultDisplay();

        // workaround for my older API 10 smartphone
        GlobalInformation.setScreenHeight(display.getHeight());
        GlobalInformation.setScreenWidth(display.getWidth());
        GlobalInformation.setCurrentRoom(0);

        //VOR dem Subjekt müssen die Räume erzeugt werden, sonst funktioniert die Navigation nicht
        //fillRoomList();
        InformationPublisher.init_InformationPublisher(this.getApplicationContext(),"house.xml");
        InformationPublisher.setRoomlist(null);

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
                if (paused == false) {
                    subject.tick();

                    List<Integer> items = new ArrayList<>();
                    int itemCount = 0;
                    try{
                        items = (World.getRoomById(GlobalInformation.getCurrentRoom())).getContainingitems();
                        itemCount = items.size();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    for(int i = 0; i < itemCount; i++){
                        Item item = World.getItemById(items.get(i));
                        item.tick();
                    }

                    grafik.postInvalidate();

                    InternalClock.computeTime();
                }

                if (GlobalInformation.getToastMessage() != "void"){
                    final String message = GlobalInformation.getToastMessage();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    GlobalInformation.setToastMessage("void");
                }
            }
        }, 0, tick);
    }



    @Override
    public void onBackPressed() {
        //Kills the app immediately
        //TODO figure out a way to do this more safely and elegant
        super.onBackPressed();
        timer.cancel();             //Timer sauber beenden
        this.finish();
        System.exit(0);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        paused = false;
    }

    @Override
    public void onStop(){
        super.onStop();
        paused = true;
    }
}