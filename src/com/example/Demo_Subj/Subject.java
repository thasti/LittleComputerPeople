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
    private float xDest = xPos;
    private int destRoomID;
    private Dijkstra dijkstra;

    private List<Room> route;
    private int routeRoomNum = 0;

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

    public void tick() {
        intel.tick();

        int lower = GlobalInformation.getCurrentRoom();
        int upper = GlobalInformation.getCurrentRoom();
        int left = GlobalInformation.getCurrentRoom();
        int right = GlobalInformation.getCurrentRoom();

        try{
            lower = World.getRoomById(GlobalInformation.getCurrentRoom()).getLowerRoomID();
            upper = World.getRoomById(GlobalInformation.getCurrentRoom()).getUpperRoomID();
            left = World.getRoomById(GlobalInformation.getCurrentRoom()).getLeftRoomID();
            right = World.getRoomById(GlobalInformation.getCurrentRoom()).getRightRoomID();
        }catch (NullPointerException e){
             e.printStackTrace();
        }

        // TODO hier sollte das Animations-Zeugs eigentlich nicht gemacht werden, nur die Bewegung
        // die Animation sollte das GraphicalOutput dann aus dem Zustand und der Richtung erzeugen (oder?)
        if((xPos == xDest) && (GlobalInformation.getCurrentRoom() == destRoomID)){
            routeRoomNum = 0;
            // we finished the last action, so get the next from the KI
            //ToDO: Schnittstelle muss auf getNextObject() umgestellt werdne; ist aber noch nicht genau spezifiziert
            SubjectMoveAction nextAction = intel.getNextAction();
            setDest(nextAction.getDestX(), nextAction.getDestRoom());
            route = dijkstra.dijkstra(World.getRoomById(GlobalInformation.getCurrentRoom()), World.getRoomById(destRoomID));
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
        else if (GlobalInformation.getCurrentRoom() != destRoomID){
            if (xPos == 0){
                routeRoomNum++;
                GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                xPos = GlobalInformation.getScreenWidth() - 1;//kann nicht GlobalInformation.getScreenWidth() sein sonst geht die Fkt unten wieder rein
                sound.startSound(R.raw.sound_door);
            }
            else if (xPos == (GlobalInformation.getScreenWidth())){
                routeRoomNum++;
                GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                xPos = 1;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                sound.startSound(R.raw.sound_door);
            }
            else if ((xPos == (GlobalInformation.getScreenWidth())) &&
                    ((route.get(routeRoomNum + 1).getID() == lower) ||
                    (route.get(routeRoomNum + 1).getID() == upper))){
                routeRoomNum++;
                GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                xPos = 1;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                //sound.startSound(R.raw.sound_door);
            }
            else{
                if (route.get(routeRoomNum + 1).getID() == right){
                    direction = 1;
                    xPos++;
                }
                else if (route.get(routeRoomNum + 1).getID() == left){
                    direction = -1;
                    xPos--;
                }
            }
        }
    }

    //kann das Ziel des Subjektes verändern
    private void setDest(float x, int room){
        xDest = x;
        destRoomID = room;
    }

    public List<Integer> getPictureWalkID(){
        return this.subjWalkBitInt;
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
}
