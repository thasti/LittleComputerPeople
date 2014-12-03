package com.example.Demo_Subj;

/**
 * Created by thasti-note on 29.11.2014.
 */
public class SubjectMoveAction extends SubjectAction {
    int destRoom;
    int destX;
    //Item destItem;

    public SubjectMoveAction(int m_destX, int m_destRoom) {
        destRoom = m_destRoom;
        destX = m_destX;
    }

    /*
    //TODO - JS Prototyp - Vorraussetzungen noch nicht erfüllt um Funktion zu implementieren.
    public SubjectMoveAction(Item i) {
        destItem = i;
        destX = destItem.getxPos();
        //Hole Raum des Objektes
        XMLParser.getRoomOfItemByID();
        //oder
        destRoom = destItem.getRoom();
    }
     */

    public int getDestRoom() {
        return destRoom;
    }

    public int getDestX() {
        return destX;
    }

    /*
    public Item getDestItem() {
        return destItem;
    }
    */
}
