package com.example.Demo_Subj;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {


    private List<Integer> subjWalkBitInt;
    private int subjStandBitInt;

    private int direction = 0;
    private float xPos = (float)(GlobalInformation.getScreenWidth()*0.5);
    private float yPos = (float)(GlobalInformation.getScreenHeight()*0.3);

    private Dijkstra dijkstra;

    private List<Room> route;
    private int routeRoomNum = 0;

    private int holdAnimation = 0;
    private static int holdAnimationCycles = 100;

    private Intelligence intel;

    private Sound sound;


    public Subject(Context ctx){
        intel = new Intelligence();
        sound = new Sound(ctx);                     //Zum abspielen von Sound

        fillWalkingIntegerLists();
        
        dijkstra = new Dijkstra();
    }

    private void fillWalkingIntegerLists(){

        subjWalkBitInt = new ArrayList<Integer>();
        subjWalkBitInt.add(R.drawable.walk_1);
        subjWalkBitInt.add(R.drawable.walk_2);
        subjWalkBitInt.add(R.drawable.walk_3);
        subjWalkBitInt.add(R.drawable.walk_4);
        subjStandBitInt = R.drawable.subjekt;
    }

    public void tick(){
        intel.tick();                   //KI aufrufen

        if(direction == 0){
            holdAnimation++;
            if(holdAnimation == holdAnimationCycles){
                direction = 1;
                holdAnimation = 0;
            }
        }
        if(direction == 1){
            holdAnimation++;
            xPos++;
            if(holdAnimation == holdAnimationCycles*2){
                direction = -1;
                holdAnimation = 0;
            }
        }
        if(direction == -1){
            holdAnimation++;
            xPos--;
            if(holdAnimation == holdAnimationCycles*2){
                direction = 0;
                holdAnimation = 0;
            }
        }
        //Nach dem KI-Aufruf müssen direction, x und y-Koordinate gesetzt werden
    }

    public List<Integer> getPictureWalkID(){
        return this.subjWalkBitInt;
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
            routeRoomNum = 0;
            aktBitmap = subjStandBitmap;
            listPointerWalk = 0;
            // we finished the last action, so get the next from the KI
            //ToDO: Schnittstelle muss auf getNextObject() umgestellt werdne; ist aber noch nicht genau spezifiziert
            SubjectMoveAction nextAction = intel.getNextAction();
            setDest(nextAction.getDestX(), nextAction.getDestRoom());
            route = dijkstra.dijkstra(World.getRoomByIdJS(aktRoomID), World.getRoomByIdJS(destRoomID));
            // move.getDestItem().use();
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
        else if (aktRoomID != destRoomID){
            if (xPos == 0){
                aktBitmap = subjStandBitmap;
                routeRoomNum++;
                aktRoomID = route.get(routeRoomNum).getId();
                xPos = GlobalInformation.getScreenWidth() - 1;//kann nicht GlobalInformation.getScreenWidth() sein sonst geht die Fkt unten wieder rein
                //reset walking animation
                listPointerWalk = 0;
                startSound(R.raw.sound_door);
            }
            else if (xPos == (GlobalInformation.getScreenWidth())){
                aktBitmap = subjStandBitmapInv;
                routeRoomNum++;
                aktRoomID = route.get(routeRoomNum).getId();
                xPos = 1;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                //reset walking animation
                listPointerWalk = 0;
                startSound(R.raw.sound_door);
            }
            else if ((xPos == (GlobalInformation.getScreenWidth())) &&
                    ((route.get(routeRoomNum + 1).getId() == World.getRoomById(aktRoomID).getLowerRoomID()) ||
                     (route.get(routeRoomNum + 1).getId() == World.getRoomById(aktRoomID).getUpperRoomID()))){
                aktBitmap = subjStandBitmapInv;
                routeRoomNum++;
                aktRoomID = route.get(routeRoomNum).getId();
                xPos = 1;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                //reset walking animation
                listPointerWalk = 0;
                startSound(R.raw.sound_door);
            }
            else{
                if (route.get(routeRoomNum + 1).getId() == World.getRoomByIdJS(aktRoomID).getRightRoomID()){
                    swapBitmap(true);
                    xPos++;
                }
                else if (route.get(routeRoomNum + 1).getId() == World.getRoomByIdJS(aktRoomID).getLeftRoomID()){
                    swapBitmap(false);
                    xPos--;
                }
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
