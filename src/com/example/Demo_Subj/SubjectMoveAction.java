package com.example.Demo_Subj;

/**
 * Created by thasti-note on 29.11.2014.
 */
public class SubjectMoveAction extends SubjectAction {
    int destRoom;
    int destX;

    public SubjectMoveAction(int m_destX, int m_destRoom) {
        destRoom = m_destRoom;
        destX = m_destX;
    }

    public int getDestRoom() {
        return destRoom;
    }

    public int getDestX() {
        return destX;
    }
}
