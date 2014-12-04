package com.example.Demo_Subj;

import android.graphics.Point;

/**
 * Created by thasti-note on 29.11.2014.
 */
public class KI {
    private int state;

    public KI() {
        state = 0;
    }

    public SubjectMoveAction getNextAction() {
        switch (state) {
            case 0:
                state = 1;
                return new SubjectMoveAction(680, 2);
            case 1:
                state = 0;
                return new SubjectMoveAction(40, 1);
        }
        return new SubjectMoveAction(0,1);
    }

    public void tick() {
        // do the internal KI logic here (needs, wishes, blabla)
        // and edit upcoming actions accordingly
    }
}
