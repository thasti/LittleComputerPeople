package com.example.Demo_Subj;

/*
 *Autor: Jürgen Ullmann
 */

public class InternalClock {

    private static int tick = 10;                               //Dauer eines Ticks in Millisekunden
    private static int realTimeDay = 4;                         //Dauer eines internen Tages in realen Minuten
    private static int realTimeNight = 1;                       //Dauer einer internen Nacht in realen Minuten


    private static int tickAmountDay;                           //Benötigte Ticks für 1 internen Tag
    private static int tickAmountNight;                         //Benötigte Ticks für 1 interne Nacht
    private static int tickCount = 0;
    private static boolean day = true;


    public static int getTick(){                                //Übergebe den Tick
        return tick;
    }

    public static void init(){
        checkInformation();
        computeDay();
        computeNight();
    }

    private static void checkInformation(){                     //Check and correct information
        if(tick > 100)  tick = 100;
        if(tick < 1)    tick = 1;
        if (realTimeDay < 1)    realTimeDay = 1;
        if (realTimeNight < 1)  realTimeNight = 1;
    }

    private static void computeDay(){                           //Compute the amount of ticks needed for a day
        tickAmountDay = (1000*60*realTimeDay)/ tick;
    }

    private static void computeNight(){                         //Compute the amount of ticks needed for a night
        tickAmountNight = (1000*60*realTimeNight)/ tick;
    }


    public static void computeTime(){                           //Compute the internal time, has to be called every tick from the timer

        tickCount++;
        if((day) && (tickCount >= tickAmountDay)){
            day = false;                                        //Nacht
            tickCount = 0;
        }

        if((!day) && (tickCount >= tickAmountNight)){
            day = true;                                         //Tag
            tickCount = 0;
        }
    }

    public static boolean isDay(){                              //Gibt einen boolschen Wert zurück
        return day;                                             //true  -> es ist Tag
    }                                                           //false -> es ist Nacht
}
