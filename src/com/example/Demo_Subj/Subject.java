package com.example.Demo_Subj;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {

    //aktuelle Position
    private float xPos;
    private float yPos;
    private int aktRoomID = 1;

    private List<Integer> subjWalkBitInt;
    private int subjStandBitInt;

    //Bildverweise
    //TODO: List/Array of bitmaps for animations
    private Bitmap subjStandBitmap;
    private Bitmap subjStandBitmapInv;

    private List<Bitmap> subjectWalk;
    private List<Bitmap> subjectWalkInv;

    private List<Room> route;
    private int routeRoomNum = 0;

    private Intelligence intel;

    private Sound sound;


        for (int i = 0; i <= (bitmaps.size() - 1); i++){
            Bitmap bm = bitmaps.get(i);
            subjectWalk.add(i,  Bitmap.createScaledBitmap(bm, GlobalInformation.getScreenWidth()/5, (int)(GlobalInformation.getScreenHeight()/1.5), false));
        }

        for (int i = 0; i <= (subjectWalk.size() - 1); i++){
            Bitmap bm = subjectWalk.get(i);
            bm = this.mirrorBitmap(bm);
            subjectWalkInv.add(i, bm);
        }

        float canvasx = (float) GlobalInformation.getScreenWidth();
        float canvasy = (float) GlobalInformation.getScreenHeight();
        float bitmapx = (float) subjStandBitmap.getWidth();
        float bitmapy = (float) subjStandBitmap.getHeight();
        float posX = ((canvasx/2) - (bitmapx / 2));
        float posY = (((canvasy/2) - (bitmapy / 2)) + (canvasy/15));
        setDefaultKoords(posX, posY, 1);

        intel = new Intelligence();
        sound = new Sound(ctx);                     //Zum abspielen von Sound

        mediaplayer = new MediaPlayer();
    }

        xPos = (float)(GlobalInformation.getScreenWidth()*0.5);
        xDest = (float)(GlobalInformation.getScreenWidth()*0.5);
        GlobalInformation.setCurrentRoom(0);
        destRoomID = GlobalInformation.getCurrentRoom();

        dijkstra = new Dijkstra();
    }

    public void setDefaultKoords(float xDef, float yDef, int room) {
        xPos = xDef;
        yPos = yDef;
        aktRoomID = room;

        subjWalkBitInt = new ArrayList<Integer>();
        subjWalkBitInt.add(R.drawable.walk_1);
        subjWalkBitInt.add(R.drawable.walk_2);
        subjWalkBitInt.add(R.drawable.walk_3);
        subjWalkBitInt.add(R.drawable.walk_4);
        subjStandBitInt = R.drawable.subjekt;
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
    public void setDest(float x, int room) {
        xDest = x;
        destRoomID = room;
    }
    public int getPictureStandID(){
        return this.subjStandBitInt;
    }

    public int getDirection(){
        return this.direction;
    }

    public float getXPos(){
        return xPos;
    }

    public float getYPos(){
        return yPos;
    }

    public void tick() {

        int lower = GlobalInformation.getCurrentRoom();
        int upper = GlobalInformation.getCurrentRoom();

        try{
            lower = World.getRoomById(GlobalInformation.getCurrentRoom()).getLowerRoomID();
            upper = World.getRoomById(GlobalInformation.getCurrentRoom()).getUpperRoomID();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        intel.tick();

        // TODO hier sollte das Animations-Zeugs eigentlich nicht gemacht werden, nur die Bewegung
        // die Animation sollte das GraphicalOutput dann aus dem Zustand und der Richtung erzeugen (oder?)
        if((xPos == xDest) && (aktRoomID == destRoomID)){
            aktBitmap = subjStandBitmap;
            listPointerWalk = 0;
            // we finished the last action, so get the next from the KI
            //ToDO: Schnittstelle muss auf getNextObject() umgestellt werdne; ist aber noch nicht genau spezifiziert
            SubjectMoveAction nextAction = intel.getNextAction();
            setDest(nextAction.getDestX(), nextAction.getDestRoom());
            // move.getDestItem().use();
        }
        else if (GlobalInformation.getCurrentRoom() == destRoomID) {
            if (xPos > xDest) {
                direction = -1;
                xPos --;
            }
            else{
                direction = 1;
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
                xPos = GlobalInformation.getScreenWidth();
                //reset walking animation
                //startSound(R.raw.sound_door);
            }
        }
        else if (aktRoomID < destRoomID){
            if (xPos < (GlobalInformation.getScreenWidth())){
                swapBitmap(true);
                xPos++;
            }
            else{
                aktBitmap = subjStandBitmapInv;
                aktRoomID++;
                xPos = 0;
                //reset walking animation
                listPointerWalk = 0;
                startSound(R.raw.sound_door);
            }
            else{
                if (route.get(routeRoomNum + 1).getID() == World.getRoomById(GlobalInformation.getCurrentRoom()).getRightRoomID()){
                    direction = 1;
                    xPos++;
                }
                else if (route.get(routeRoomNum + 1).getID() == World.getRoomById(GlobalInformation.getCurrentRoom()).getLeftRoomID()){
                    direction = -1;
                    xPos--;
                }
            }
        }
    }
}
