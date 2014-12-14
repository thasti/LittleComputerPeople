package com.example.Demo_Subj;

public class InternalClock {
    private int tick;
    private int realTimeDay;                //Dauer eines Internen Tages in realen Minuten
    private int realTimeNight;              //Dauer einer Internen Nacht in realen Minuten
    private int tickAmountDay;              //Benötigte Ticks für 1 internen Tag
    private int tickAmountNight;            //Benötigte Ticks für 1 interne Nacht
    private int tickCount = 0;
    private boolean day = true;

    public void computeTime(){
        /*
        tick = GlobalInformation.getTick();
        realTimeDay = GlobalInformation.getRealTimeDay();
        realTimeNight = GlobalInformation.getRealTimeNight();

        if(tick > 100)  tick = 100;
        if(tick < 1)    tick = 1;
        if (realTimeDay > 1)    realTimeDay = 1;
        if (realTimeNight > 1)  realTimeNight = 1;
        */
        this.getGlobalInformation();
        this.checkGlobalInformation();
        this.computeDay();
        this.computeNight();
    }

    private void getGlobalInformation(){                            //Get information from GlobalInformation class
        tick = GlobalInformation.getTick();
        realTimeDay = GlobalInformation.getRealTimeDay();
        realTimeNight = GlobalInformation.getRealTimeNight();
    }

    private void checkGlobalInformation(){                          //Check and correct information from GlobalInformation class
        if(tick > 100)  tick = 100;
        if(tick < 1)    tick = 1;
        if (realTimeDay < 1)    realTimeDay = 1;
        if (realTimeNight < 1)  realTimeNight = 1;
    }

    private void computeDay(){                                      //Compute the amount of ticks needed for a day
        //realTimeDay = GlobalInformation.getRealTimeDay();
        tickAmountDay = (1000*60*realTimeDay)/ tick;
    }

    private void computeNight(){                                    //Compute the amount of ticks needed for a night
        //realTimeNight = GlobalInformation.getRealTimeNight();
        tickAmountNight = (1000*60*realTimeNight)/ tick;
    }

    /*
    public int getTickDay(){
        return tickAmountDay;
    }

    public int getTickNight(){
        return tickAmountNight;
    }
    */

    public boolean internalTime(){                                  //Compute the internal time, has to be called every tick from the timer

        tickCount++;
        if((day) && (tickCount >= tickAmountDay)){
            day = false;
            tickCount = 0;
        }

        if((!day) && (tickCount >= tickAmountNight)){
            day = true;
            tickCount = 0;
        }

        return day;
    }
}
