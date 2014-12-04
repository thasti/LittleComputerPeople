package com.example.Demo_Subj;

/**
 * Created by Michael on 02.12.2014.
 */

//TODO: Diese Klasse habe ich nur für Tests erzeugt, damit das kompilieren klappt. Mathias ersetzt diese noch mit seinem Code ! ! ! !

public class Need {
    private int     top_level; 	    //Schwellenwert
    private int     motivation; 	//Motivation (Startwert = 0)
    private byte    priority; 	    //Priorität
    private String  description;    //Beschreibung/ Name des Bedürfnises
    private int     object_ID;	    //Objekt, welches das Bedürfnis braucht zum befrieedigen
                                        //formuliert als ID, welche der Engien itgeteilt werden muss
    private boolean day_night;	    //boolean für Tag (1) oder Nachtaktiv (0)
    private int     current_value;	//aktueller Wert des Bedürfnisses (Startwert = 0)

    public Need(int lokal_top_level, byte lokal_priority, String lokal_description, int lokal_object_ID, boolean lokal_day_night){
        motivation    = 0;
        current_value = 0;

        top_level   = lokal_top_level;
        priority    = lokal_priority;
        description = lokal_description;
        object_ID   = lokal_object_ID;
        day_night   = lokal_day_night;
    }

    public int get_top_level(){ return top_level;}

    public int get_motivation(){ return motivation;}
    public void set_motivation(int moti){ motivation = moti;}

    public byte get_priority(){ return priority;}

    public int get_object_id(){ return object_ID;}

    public boolean get_day_night(){ return day_night;}

    public int get_current_value(){ return current_value;}
    public void set_current_value(int val){current_value = val;}
}
