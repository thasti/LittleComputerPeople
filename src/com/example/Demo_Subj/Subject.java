package com.example.Demo_Subj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;


/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {

    //aktuelle Position
    private float xPos;
    private float yPos;
    private int aktRoomID;

    //Ziel
    private float xDest;
    private int destRoomID;

    //Bildverweise
    // TODO: List/Array of bitmaps for animations
    private Bitmap subjStandBitmap;
    private Bitmap subjStandBitmapInv;
    private Bitmap subjWalk1Bitmap;
    private Bitmap subjWalk1BitmapInv;
    private Bitmap subjWalk2Bitmap;
    private Bitmap subjWalk2BitmapInv;


    private Bitmap aktBitmap;
    private int initialized;
    private int screenWidth;

    private Context context;
    private SoundManager soundmanager;

    private KI intel;

    //Konstruktor: zu übergeben - 3 Bilder
    public Subject(Bitmap sStand, int screenW, Context c){
        subjStandBitmap = sStand;
        subjStandBitmapInv = mirrorBitmap(subjStandBitmap);

        aktBitmap = sStand;
        aktRoomID = 1;
        initialized = 0;
        screenWidth = screenW;
        context = c;
        soundmanager = new SoundManager(c);
        soundmanager.setBalance(1);
        soundmanager.setVolume(1);

        intel = new KI();
    }

    public void setDefaultKoords(float xDef, float yDef, int room){
        xPos = xDef;
        yPos = yDef;
        aktRoomID = room;

        xDest = xDef;
        destRoomID = room;
    }

    public void setInitialized(){
        initialized = 1;
    }
    public int getInitialized(){
        return initialized;
    }

    public int reachedDest(){
        if ((xPos == xDest) && (aktRoomID == destRoomID)){
            return 1;
        }
        else {
            return 0;
        }
    }


    //kann das Ziel des Subjektes verändern
    public void setDest(float x, int room){
        xDest = x;
        destRoomID = room;
    }

    //gibt die Y Koordinate im Raum zurück an der sich das Subjekt gerade befindet
    public float getxPos() {
        return xPos;
    }

    //gibt die Y Koordinate im Raum zurück an der sich das Subjekt gerade befindet
    public float getyPos() {
        return yPos;
    }

    //gibt den Raum zurück in dem sich das Subjekt gerade befindet
    public int getAktRoomID() {
        return aktRoomID;
    }

    public Bitmap getSubjBitmap(){
        return aktBitmap;
    }

    public void tick() {
        intel.tick();

        // TODO hier sollte das Animations-Zeugs eigentlich nicht gemacht werden, nur die Bewegung
        // die Animation sollte das GraphicalOutput dann aus dem Zustand und der Richtung erzeugen (oder?)
        if((xPos == xDest) && (aktRoomID == destRoomID)){
            aktBitmap = subjStandBitmap;
            // we finished the last action, so get the next from the KI
            SubjectAction nextAction = intel.getNextAction();
            if (nextAction instanceof SubjectMoveAction) {
                SubjectMoveAction move = (SubjectMoveAction)nextAction;
                setDest(move.getDestX(), move.getDestRoom());
            }
            // TODO: else -> implement SubjectDoAction
        }
        else if (aktRoomID == destRoomID) {
            if (xPos > xDest) {
                aktBitmap = subjStandBitmap;
                xPos --;
            }
            else{
                aktBitmap = subjStandBitmapInv;
                xPos++;
            }
        }
        else if (aktRoomID > destRoomID){
            if (xPos > 0){
                aktBitmap = subjStandBitmap;
                xPos--;
            }
            else{
                aktBitmap = subjStandBitmap;
                aktRoomID--;
                soundmanager.play(soundmanager.load(R.raw.sound_door));
                xPos = screenWidth;
            }
        }
        else if (aktRoomID < destRoomID){
            if (xPos < (screenWidth)){
                aktBitmap = subjStandBitmapInv;
                xPos++;
            }
            else{
                aktBitmap = subjStandBitmapInv;
                aktRoomID++;
                soundmanager.play(soundmanager.load(R.raw.sound_door));
                xPos = 0;
            }
        }
    }

    private void swapBitmap(boolean mirror){
        if ((aktBitmap == subjWalk1Bitmap)||(aktBitmap == subjWalk1BitmapInv)){
            if (mirror == true){
                aktBitmap = subjWalk2BitmapInv;
            }
            else
                aktBitmap = subjWalk2Bitmap;
        }
        else{
            if (mirror == true){
                aktBitmap = subjWalk1BitmapInv;
            }
            else
                aktBitmap = subjWalk1Bitmap;
        }
    }

    private Bitmap mirrorBitmap(Bitmap b){
        Matrix matrixMirror = new Matrix();
        matrixMirror.preScale(-1.0f, 1.0f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrixMirror, false);
        return b;
    }
}
