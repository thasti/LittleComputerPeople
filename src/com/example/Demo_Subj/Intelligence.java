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
    private int	counterDayNight;	    //Zählvariable für den Tick
    private int	limitDay;		        //hier steht die Grenze, wie lange ein Tag dauern soll in Ticks; wird mit Tick_milsec im Konstruktor berechnet
    private int	limitNight;		    //hier steht die Grenze, wie lange eine Nacht dauern soll in Ticks; wird mit Tick_milsec im Konstruktor berechnet
    private boolean boolDayNight;          //Zustandvariable, ob gerade Tag (1) oder Nacht (0) ist

    private final int DAY_TIME = 5*60;   //Ein Tag soll 5 Minuten dauern; Angabe ist in Sekunden
    private final int NIGHT_TIME = 1*60; //Eine Nacht soll 1 Minute dauern, Angabe ist in Sekunden


    //Standradkonstrktor - nimmt Werte, die sich als sinnvoll bzw logisch ergeben haben
    public Intelligence() {

        //Initialisierung des Vectors
        initVector();

        //Initialisierung privater Attribute mit eigens festgelegten Werten:
        initAttributes(1, 10, 100); //Erzeugung von Zufallszahlen zwischen 1 und 10; Tick alle 100ms
    }

    //überladener Konstruktor - für die Tests, um die Intelligence verschieden zu konfigurieren
    public Intelligence(int randomStart, int randomEnd, int Tick_milsec){
        //Initialisierung des Vectors
        initVector();

        //Initialisierung privater Attribute:
        initAttributes(randomStart, randomEnd, Tick_milsec);
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
    private void initAttributes(int lokal_randomStart, int lokal_randomEnd, int lokal_Tick_milsec){
        int temp;       //zum Vertauschen von lokal_randomEnd und lokal_randomStart

        maxMotivationIndex = 0;
        counterDayNight = 0;
        boolDayNight = true;               //beginnend mit Tag


        if(lokal_randomStart < 0)                        //lokal_randomStart ist negativ -> * -1
            lokal_randomStart = lokal_randomStart * -1;

        if(lokal_randomEnd < 0)                          //lokal_randomEnd ist negativ -> * -1
           lokal_randomEnd = lokal_randomEnd * -1;

        if(lokal_randomStart > lokal_randomEnd){        //lokal_randomStart ist größer als lokal_randomEnd -> vertauschen
            temp = lokal_randomStart;
            lokal_randomStart = lokal_randomEnd;
            lokal_randomEnd = temp;
        }

        if(lokal_randomEnd == lokal_randomStart)        //lokal_randomStart ist identisch lokal_randomEnd -> lokal_randomEnd + 1
            lokal_randomEnd++;

        randomStart = lokal_randomStart;
        randomEnd = lokal_randomEnd;

        if(lokal_Tick_milsec < 0)                         //lokal_Tick_milsec ist negativ -> * -1
            lokal_Tick_milsec = lokal_Tick_milsec * -1;

        if(lokal_Tick_milsec == 0)                        //wurde kein Tick übergeben, wird dieser als 100 angenommen
            lokal_Tick_milsec += 100;

        limitDay    = DAY_TIME * 1000 / lokal_Tick_milsec;
        limitNight  = NIGHT_TIME * 1000 / lokal_Tick_milsec;

    }

    //Aufruf von Subjekt; Gibt Objekt_ID zurück, zu der das Subjekt als nächstes laufen soll
    public int getNextObject(){
        //ToDo: XMl parsen und XMLParser.getObjectbyID() aufrufen

        //Bedürfnis mit der höchsten Motivation resetten
        needs_list.elementAt(maxMotivationIndex).set_current_value(0);

        return needs_list.elementAt(maxMotivationIndex).get_object_id();
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
        int motivation_highest = 0;
        Need beduerfnis;

        //Index der höchsten Motivation zurücksetzen auf 0;
        maxMotivationIndex = 0;

        //feststellen, ob gerade Tag oder Nacht ist
        setboolDayNight();

        //Liste inkrementieren
        for(int i=0; i < needs_list.size(); i++) {
            //bedürfnis aus Liste nehmen
            beduerfnis = needs_list.elementAt(i);

            //Prüfen ob Tag oder Nacht ist und die entsprechenden Bedürfnisse nutzen
            if (boolDayNight && beduerfnis.get_day_night()) { //es ist Tag und Bedürfnis ist tagaktiv
                motivation_highest = manageList(i, motivation_highest);

            } else if (!boolDayNight && !beduerfnis.get_day_night()) { //es ist Nacht und das Bedürfnis ist nachtaktiv
                motivation_highest = manageList(i, motivation_highest);
            }
        }
    }

    //Zufallszahl zwischen randomStart und randomEnd erzeugen
    private int getIntRandom(){
        return randomStart + (int) Math.round( Math.random() * (randomEnd - randomStart) );
    }

    //Feststellen ob Tag oder Nacht ist
    private void setboolDayNight(){
        //Zählvariable für Tick inkrementieren
        counterDayNight++;

        //Zahlvariable zurücksetzen (= boolDayNight umschalten), wenn...
        if(boolDayNight && counterDayNight >= limitDay){  //... Tag ist und die Zählvaribale das Tageslimit erreicht hat
            counterDayNight = 0;
            boolDayNight = false;
        }else if(!boolDayNight && counterDayNight >= limitNight){//... Nahct  ist und die Zählvariable das Nachtlimit erreicht aht
            counterDayNight = 0;
            boolDayNight = true;
        }

        //sonst bleibt alles beim alten; das heißt: Tag/ NAcht geht weiter
    }

    //Werte des Bedürfnisses an der Stelle need_list[index] werden geändert und motivation_highest neu berechnet
    private int manageList(int index, int motivation_highest){
        //es kann nicht das Objekt "beduerfnis" übergeben werden, da sich die eigenschfaten des Objektes ändern müssen. Dies soll dierekt in der Liste geschehen.

        //aktuellen Wert ändern; aktueller Wert = aktueller Wert + Zufallszahl
        needs_list.elementAt(index).set_current_value(needs_list.elementAt(index).get_current_value() + getIntRandom());

        //Motivation neu berechnen; Motivation = Priorität * (aktueller Wert - Schwellwert)
        needs_list.elementAt(index).set_motivation(needs_list.elementAt(index).get_priority() * (needs_list.elementAt(index).get_current_value() - needs_list.elementAt(index).get_top_level()));

        //Höchte Motivation finden:
        //neuer index, wenn neue höchste Motivation
        if(needs_list.elementAt(index).get_motivation() > motivation_highest){
            maxMotivationIndex = index;
            motivation_highest = needs_list.elementAt(index).get_motivation();
            //neuer Index, wenn Motivationen übereinstimmen UND Priorität des neuen Bedürfnisses größer ist der Priorität des alten Bedürfnisses
        }else if(needs_list.elementAt(index).get_motivation() == motivation_highest && (needs_list.elementAt(index).get_priority() > needs_list.elementAt(maxMotivationIndex).get_priority())){
            maxMotivationIndex = index;
            motivation_highest = needs_list.elementAt(index).get_motivation();
        }

        return motivation_highest;
    }

}
