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
    private float xPos = (float) (GlobalInformation.getScreenWidth() * 0.5);
    private float yPos = (float) (GlobalInformation.getScreenHeight() * 0.3);
    private float xDest = xPos;
    private int destRoomID;
    private Dijkstra dijkstra;
    private Item destItem;
    private Item prevItem;
    private Integer wait = 0;

    private int subjectSpeed = 3;
    private Sound sound;

    private List<Room> route;
    private int routeRoomNum = 0;

    private Intelligence intel;
    private Context context;


    public Subject(Context ctx) {
        context = ctx;
        intel = new Intelligence();

        World.setIntelligence(intel);

        sound = new Sound(ctx);

        fillWalkingIntegerLists();

        dijkstra = new Dijkstra();

    }

    private void fillWalkingIntegerLists() {

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

        try {
            lower = World.getRoomById(GlobalInformation.getCurrentRoom()).getLowerRoomID();
            upper = World.getRoomById(GlobalInformation.getCurrentRoom()).getUpperRoomID();
            left = World.getRoomById(GlobalInformation.getCurrentRoom()).getLeftRoomID();
            right = World.getRoomById(GlobalInformation.getCurrentRoom()).getRightRoomID();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //Warten bis die Animation vom vorherigen Objekt zuende ist
        if (wait == 1){
            direction = 0;
            int used = 0;
            try{
                used = prevItem.isUsed();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            if (used == 0){
                wait = 0;
            }
        }

        if (wait == 0){
            // TODO hier sollte das Animations-Zeugs eigentlich nicht gemacht werden, nur die Bewegung
            // die Animation sollte das GraphicalOutput dann aus dem Zustand und der Richtung erzeugen (oder?)
            if (((xPos == xDest) | ((xPos <= xDest + subjectSpeed) && (xPos >= xDest - subjectSpeed))) && (GlobalInformation.getCurrentRoom() == destRoomID)) {
                wait = 1;

                try{
                    destItem.useBySubject();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                int soundID = 0;
                try{
                    soundID = destItem.getSoundRes();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                if(soundID != 0) {
                    sound.startSound(soundID);
                }

                routeRoomNum = 0;
                direction = 0;

                // we finished the last action, so get the next from the KI
                //ToDO: Schnittstelle muss auf getNextItem() umgestellt werdne; ist aber noch nicht genau spezifiziert

                //Ich muss das alte Ziel noch speichern - da ich warten muss bis dessen animation vorbei ist, bevor ich mich dann zum neuen Ziel
                try {
                    prevItem = destItem;
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                destItem = null;
                try {
                    destItem = intel.getNextItem();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (destItem == null) {
                    SubjectMoveAction nextAction = intel.getNextAction();
                    setDest(nextAction.getDestX(), nextAction.getDestRoom());
                } else {
                    //Quasi ein getRoomByItem()- muss dann in World rein
                    for (int i = 0; i < World.getAllRooms().size(); i++) { //geht jeden Raum durch
                        Room j = World.getRoomById(i);
                        for (int k = 0; k < j.getContainingitems().size(); k++) {        //geht jedes Objekt im aktuellen Raum durch
                            if (destItem.getID() == j.getContainingitems().get(k)) {    //wenn die ItemID im aktuellen Raum ist
                                destRoomID = i;                                          //dann ist der destRoom = i;
                            }
                        }
                    }
                    setDest(destItem.getXPos().floatValue(), destRoomID);
                }
                route = dijkstra.dijkstra(World.getRoomById(GlobalInformation.getCurrentRoom()), World.getRoomById(destRoomID));
                System.out.println(destItem.getNeed());
                GlobalInformation.setToastMessage("I want " + destItem.getNeed() + " now!");


                // move.getDestItem().use();
            } else if (GlobalInformation.getCurrentRoom() == destRoomID) {
                if (xPos > xDest) {
                    direction = -1;
                    xPos = xPos - subjectSpeed;
                } else {
                    direction = 1;
                    xPos = xPos + subjectSpeed;
                }
            } else if (GlobalInformation.getCurrentRoom() != destRoomID) {
                if (xPos <= 0) {
                    routeRoomNum++;
                    GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                    xPos = GlobalInformation.getScreenWidth() - 1;//kann nicht GlobalInformation.getScreenWidth() sein sonst geht die Fkt unten wieder rein
                } else if (xPos >= (GlobalInformation.getScreenWidth())) {
                    routeRoomNum++;
                    GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                    xPos = 1;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                } else if (((xPos >= ((GlobalInformation.getScreenWidth() / 2) - subjectSpeed)) &&(xPos <= ((GlobalInformation.getScreenWidth() / 2) + subjectSpeed))) &&
                        ((route.get(routeRoomNum + 1).getID() == lower) ||
                        (route.get(routeRoomNum + 1).getID() == upper))){
                    routeRoomNum++;
                    GlobalInformation.setCurrentRoom(route.get(routeRoomNum).getID());
                    xPos = GlobalInformation.getScreenWidth() / 2;//kann nicht 0 sein sonst geht die Fkt oben wieder rein (xPos == 0)
                } else {
                    if (route.get(routeRoomNum + 1).getID() == right) {
                        direction = 1;
                        xPos += subjectSpeed;
                    } else if (route.get(routeRoomNum + 1).getID() == left) {
                        direction = -1;
                        xPos -= subjectSpeed;
                    } else if ((route.get(routeRoomNum + 1).getID() == lower) ||
                            (route.get(routeRoomNum + 1).getID() == upper)) {
                        if (xPos > (GlobalInformation.getScreenWidth() / 2)) {
                            direction = -1;
                            xPos -= subjectSpeed;
                        } else {
                            direction = 1;
                            xPos += subjectSpeed;
                        }
                    }
                }
            }
        }
    }

    //kann das Ziel des Subjektes verändern
    private void setDest(float x, int room) {
        xDest = x;
        destRoomID = room;
    }

    public List<Integer> getPictureWalkID() {
        return this.subjWalkBitInt;
    }

    public int getPictureStandID() {
        return this.subjStandBitInt;
    }

    public int getDirection() {
        return this.direction;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }
}

