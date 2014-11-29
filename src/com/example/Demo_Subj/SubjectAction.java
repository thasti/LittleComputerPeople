package com.example.Demo_Subj;

/**
 * Created by thasti-note on 29.11.2014.
 */
public abstract class SubjectAction {
    private int actionID;

    public SubjectAction(int m_actionID) {
        actionID = m_actionID;
    }

    public int getActionID() {
        return actionID;
    }
}
