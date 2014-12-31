package com.example.Demo_Subj;


import java.util.*;


/*
    TODO:      der Intelligence Pfad für bedürfnisse.xml in Konstruktor übergeben
*/

/**
 * Completely created by Michael on 02.12.2014.
 */
public class Intelligence {
    private int state = 0;      //Zustandvaribale für getNextAction; wird final nicht mehr benötigt

    private Vector<Need> needs_list = new Vector<Need>(); 		//verwaltet die Bedürfnisse als abstrakte Objekte ohne konkretem Namen als Vektor
    private int	randomStart;		    //Konstante: Startwert für Zufallszahlengenerator
    private int	randomEnd;		        //Konstante: Endwert für Zufallszahlengenerator
    private int	maxMotivationIndex;   //Der Index in der Liste, wo das Bedürfnis mit der höchsten Motivation steht


    //Standradkonstrktor - nimmt Werte, die sich als sinnvoll bzw logisch ergeben haben
    public Intelligence() {

        //Initialisierung des Vectors
        initVector();

        //Initialisierung privater Attribute mit eigens festgelegten Werten:
        initAttributes(1, 10); //Erzeugung von Zufallszahlen zwischen 1 und 10
    }

    //überladener Konstruktor - für die Tests, um die Intelligence verschieden zu konfigurieren
    public Intelligence(int randomStart, int randomEnd){
        //Initialisierung des Vectors
        initVector();

        //Initialisierung privater Attribute:
        initAttributes(randomStart, randomEnd);
    }

    //Liest die .xml und füllt den Vektor mit Bedürfnissen
    private void initVector(){
        /*ToDo:
            - InformationProvider aufrufen und fertige Liste von Bedürfnissen übergeben lassen
            - Bedürfnisse beim Instanziieren in Vektor schieben

             needs_list.addElement( new Need(top_level, priority, description, object_ID, day_night));
         */
    }

    //Belegt die privaten Attribute mit Anfangswerten
    private void initAttributes(int randomStart, int randomEnd){
        int temp;       //zum Vertauschen von this.randomEnd und this.randomStart

        maxMotivationIndex = 0;

        if(this.randomStart < 0)                        //this.randomStart ist negativ -> * -1
            this.randomStart = this.randomStart * -1;

        if(this.randomEnd < 0)                          //this.randomEnd ist negativ -> * -1
           this.randomEnd = this.randomEnd * -1;

        if(this.randomStart > this.randomEnd){        //this.randomStart ist größer als this.randomEnd -> vertauschen
            temp = this.randomStart;
            this.randomStart = this.randomEnd;
            this.randomEnd = temp;
        }

        if(this.randomEnd == this.randomStart)        //this.randomStart ist identisch this.randomEnd -> this.randomEnd + 1
            this.randomEnd++;

        randomStart = this.randomStart;
        randomEnd = this.randomEnd;
    }

    //Aufruf von Subjekt; Gibt Objekt_ID zurück, zu der das Subjekt als nächstes laufen soll
    public int getNextObject(){
        //ToDo: Welt abfragen, welches Objekt zur ObejktID gehört; Objekt zurückgeben

        //Bedürfnis mit der höchsten Motivation resetten
        needs_list.elementAt(maxMotivationIndex).setCurrentValue(0);

        return needs_list.elementAt(maxMotivationIndex).getObjectID();
    }

    //um mit dem unfertigen Projekt kompilieren zu können, wird die Method noch beibehalten
    //fliegt aber raus, sobald die Schnittstelle und der Übergabewert geklärt sind
    public SubjectMoveAction getNextAction() {
        switch (state) {
            case 0:
                state = 1;
                return new SubjectMoveAction(680, 2);
            case 1:
                state = 0;
                return new SubjectMoveAction(40, 1);
        }
        return new SubjectMoveAction(0, 1);
    }

    //Algorithmus, in dem die Intelligence den Vektor verwaltet und alle Bedürfnisse neu berechnet
    public void tick() {
        boolean day = InternalClock.isDay();

        int motivation_highest = 0;
        Need beduerfnis;

        //Index der höchsten Motivation zurücksetzen auf 0;
        maxMotivationIndex = 0;

        //Liste inkrementieren
        for(int i=0; i < needs_list.size(); i++) {
            //bedürfnis aus Liste nehmen
            beduerfnis = needs_list.elementAt(i);

            //Prüfen ob Tag oder Nacht ist und die entsprechenden Bedürfnisse nutzen
            if (day && beduerfnis.getActiveDayNight()) { //es ist Tag und Bedürfnis ist tagaktiv
                motivation_highest = manageList(i, motivation_highest);

            } else if (!day && !beduerfnis.getActiveDayNight()) { //es ist Nacht und das Bedürfnis ist nachtaktiv
                motivation_highest = manageList(i, motivation_highest);
            }
        }
    }

    //Zufallszahl zwischen randomStart und randomEnd erzeugen
    private int getIntRandom(){
        return randomStart + (int) Math.round( Math.random() * (randomEnd - randomStart) );
    }

    //Werte des Bedürfnisses an der Stelle need_list[index] werden geändert und motivation_highest neu berechnet
    private int manageList(int index, int motivation_highest){
        //es kann nicht das Objekt "beduerfnis" übergeben werden, da sich die eigenschfaten des Objektes ändern müssen. Dies soll dierekt in der Liste geschehen.

        try {
            //aktuellen Wert ändern; aktueller Wert = aktueller Wert + Zufallszahl
            needs_list.elementAt(index).setCurrentValue(needs_list.elementAt(index).getCurrentValue() + getIntRandom());

            //Motivation neu berechnen; Motivation = Priorität * (aktueller Wert - Schwellwert)
            needs_list.elementAt(index).setMotivation(needs_list.elementAt(index).getPriority() * (needs_list.elementAt(index).getCurrentValue() - needs_list.elementAt(index).getTopLevel()));
        }
        catch(Exception e){ //es sollen Exceptions JEDER Art abgefangen werden, da von dne Funktionen nicht expliziert Exceptions geworfen werden
            //TODO: Fehlerausgabe auf Android- Systemen
        }
        //Höchte Motivation finden:
        //neuer index, wenn neue höchste Motivation
        if(needs_list.elementAt(index).getMotivation() > motivation_highest){
            maxMotivationIndex = index;
            motivation_highest = needs_list.elementAt(index).getMotivation();
            //neuer Index, wenn Motivationen übereinstimmen UND Priorität des neuen Bedürfnisses größer ist der Priorität des alten Bedürfnisses
        }else if(needs_list.elementAt(index).getMotivation() == motivation_highest && (needs_list.elementAt(index).getPriority() > needs_list.elementAt(maxMotivationIndex).getPriority())){
            maxMotivationIndex = index;
            motivation_highest = needs_list.elementAt(index).getMotivation();
        }

        return motivation_highest;
    }

}
