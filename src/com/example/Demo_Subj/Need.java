package com.example.Demo_Subj;

/**
 * Created by mathias on 02.12.14.
 */
public class Need {
    private int top_level;      //Schwellwert
    private int motivation;     //Motivation Startwert = 0
    private byte priority;      //Priorität
    private String description; //Name des Bedürfnisses
    private int object_ID;      //Objekt ID muss der Engine mitgeteilt werden
    private boolean day_night;  //Tag- Nachtzyklus
    private int current_value;  //Aktueller Wert

    public Need() {
        motivation = 0;
        current_value = 0;
    }

    public Need(int top_level, byte priority, String description, int object_ID, boolean day_night) {
        this.top_level = top_level;
        this.priority = priority;
        this.description = description;
        this.object_ID = object_ID;
        this.day_night = day_night;

        motivation = 0;
        current_value = 0;
    }

    /* GET METHODEN */
    public int get_top_level() {
        return top_level;
    }

    public int get_motivation() {
        return motivation;
    }

    public byte get_priority() {
        return priority;
    }

    public String get_description() {
        return description;
    }

    public int get_object_id() {
        return object_ID;
    }

    public boolean get_day_night() {
        return day_night;
    }

    public int get_current_value() {
        return current_value;
    }

    /* SET METHODEN */
    public void set_top_level(int new_top_level) {
        if (new_top_level < 0)
            new_top_level = 0;

        top_level = new_top_level;
    }

    public void set_motivation(int new_motivation) {
        motivation = new_motivation;
    }

    public void set_priority(byte new_priority) {
        if (new_priority < 0)
            new_priority = 0;
        priority = new_priority;
    }

    public void set_description(String new_description) {
        description = new_description;
    }

    public void set_object_ID(int new_object_ID) {
        object_ID = new_object_ID;
    }

    public void set_day_night(boolean new_day_night) {
        day_night = new_day_night;
    }

    public void set_current_value(int new_current_value) {
        if (new_current_value < 0)
            new_current_value = 0;
        current_value = new_current_value;
    }
}

