package com.example.Demo_Subj;

/**
 * Created by mathias on 02.12.14.
 */
public class Need {
    private int topLevel;      //Schwellwert
    private int motivation;     //Motivation Startwert = 0
    private byte priority;      //Priorität
    private String description; //Name des Bedürfnisses
    private int objectID;      //Objekt ID muss der Engine mitgeteilt werden
    private boolean activeDayNight;  //Tag- Nachtzyklus
    private int currentValue;  //Aktueller Wert

    public Need() {
        motivation = 0;
        currentValue = 0;
    }

    public Need(int topLevel, byte priority, String description, int objectID, boolean activeDayNight) {
        this.topLevel = topLevel;
        this.priority = priority;
        this.description = description;
        this.objectID = objectID;
        this.activeDayNight = activeDayNight;

        motivation = 0;
        currentValue = 0;
    }

    /* GET METHODEN */
    public int getTopLevel() {
        return topLevel;
    }

    public int getMotivation() {
        return motivation;
    }

    public byte getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public int getObjectID() {
        return objectID;
    }

    public boolean getActiveDayNight() {
        return activeDayNight;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    /* SET METHODEN */
    public void setTopLevel(int new_topLevel) {
        if (new_topLevel < 0)
            new_topLevel = 0;

        topLevel = new_topLevel;
    }

    public void setMotivation(int new_motivation) {
        motivation = new_motivation;
    }

    public void setPriority(byte new_priority) {
        if (new_priority < 0)
            new_priority = 0;
        priority = new_priority;
    }

    public void setDescription(String new_description) {
        description = new_description;
    }

    public void setObjectID(int new_objectID) {
        objectID = new_objectID;
    }

    public void setActiveDayNight(boolean new_activeDayNight) {
        activeDayNight = new_activeDayNight;
    }

    public void setCurrentValue(int new_currentValue) {
        if (new_currentValue < 0)
            new_currentValue = 0;
        currentValue = new_currentValue;
    }
}
