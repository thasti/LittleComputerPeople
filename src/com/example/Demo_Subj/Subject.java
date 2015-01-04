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


    private int holdAnimation = 0;
    private static int holdAnimationCycles = 100;

    private Intelligence intel;

    private Sound sound;


    public Subject(Context ctx){
        intel = new Intelligence();
        sound = new Sound(ctx);                     //Zum abspielen von Sound

        fillWalkingIntegerLists();

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
