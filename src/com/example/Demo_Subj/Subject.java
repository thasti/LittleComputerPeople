package com.example.Demo_Subj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.lang.*;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {

    //aktuelle Position
    private float xPos;
    private float yPos;
    private int aktRoomID = 1;

    //Ziel
    private float xDest;
    private int destRoomID;

    //Bildverweise
    // TODO: List/Array of bitmaps for animations
    private Bitmap subjStandBitmap;
    private Bitmap subjStandBitmapInv;

    private List<Bitmap> subjectWalk;
    private List<Bitmap> subjectWalkInv;

    private Bitmap aktBitmap;
    private int initialized = 0;
    private int screenWidth;

    private int listPointerWalk = 0;
    private int holdAnimation = 0;
    private static int holdAnimationCycles = 20;

    private Context context;
    private SoundManager soundmanager;

    private KI intel;

    //Konstruktor: zu übergeben - 3 Bilder
    public Subject(Bitmap sStand, List<Bitmap> bitmaps, int screenW, Context c){
        subjStandBitmap = sStand;
        subjStandBitmapInv = mirrorBitmap(subjStandBitmap);

        aktBitmap = sStand;
        screenWidth = screenW;
        context = c;
        soundmanager = new SoundManager(c);
        soundmanager.setBalance(1);
        soundmanager.setVolume(1);

        //Copy List of Bitmaps for Walking animations
        subjectWalk = new ArrayList<Bitmap>();
        subjectWalkInv = new ArrayList<Bitmap>();

        for (int i = 0; i <= (bitmaps.size() - 1); i++){
            Bitmap bm = bitmaps.get(i);
            bm = Bitmap.createScaledBitmap(bm, 140, 350, false);
            subjectWalk.add(i, bm);
        }

        for (int i = 0; i <= (subjectWalk.size() - 1); i++){
            Bitmap bm = subjectWalk.get(i);
            bm = Bitmap.createScaledBitmap(bm, 140, 350, false);
            bm = this.mirrorBitmap(bm);
            subjectWalkInv.add(i, bm);
        }

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
            listPointerWalk = 0;
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
                swapBitmap(false);
                xPos --;
            }
            else{
                swapBitmap(true);
                xPos++;
            }
        }
        else if (aktRoomID > destRoomID){
            if (xPos > 0){
                swapBitmap(false);
                xPos--;
            }
            else{
                aktBitmap = subjStandBitmap;
                aktRoomID--;
                soundmanager.play(soundmanager.load(R.raw.sound_door));
                xPos = screenWidth;
                //reset walking animation
                listPointerWalk = 0;
            }
        }
        else if (aktRoomID < destRoomID){
            if (xPos < (screenWidth)){
                swapBitmap(true);
                xPos++;
            }
            else{
                aktBitmap = subjStandBitmapInv;
                aktRoomID++;
                soundmanager.play(soundmanager.load(R.raw.sound_door));
                xPos = 0;
                //reset walking animation
                listPointerWalk = 0;
            }
        }
    }

    private void swapBitmap(boolean mirror){
        if (mirror == true){
            if (holdAnimation < holdAnimationCycles){
                holdAnimation++;
            }
            else{
                holdAnimation = 0;
                aktBitmap = subjectWalkInv.get(listPointerWalk);
                listPointerWalk++;
                if (listPointerWalk > (subjectWalkInv.size() - 1)){
                    listPointerWalk = 0;
                }
            }
        }
        else
            if (holdAnimation < holdAnimationCycles){
                holdAnimation++;
            }
            else {
                holdAnimation = 0;
                aktBitmap = subjectWalk.get(listPointerWalk);
                listPointerWalk++;
                if (listPointerWalk > (subjectWalk.size() - 1)) {
                    listPointerWalk = 0;
                }
            }
    }

    private Bitmap mirrorBitmap(Bitmap b){
        Matrix matrixMirror = new Matrix();
        matrixMirror.preScale(-1.0f, 1.0f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrixMirror, false);
        return b;
    }
}
