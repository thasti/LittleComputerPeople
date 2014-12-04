package com.example.Demo_Subj;

//import android.graphics.Point;
import java.util.*;


/*
    TODO:      der KI Pfad für bedürfnisse.xml in Konstruktor übergeben
*/

/**
 * Completely created by Michael on 02.12.2014.
 */
public class KI {
    private int state = 0;      //Zustandvaribale für getNextAction; wird final nicht mehr benötigt

    private Vector<Need> needs_list = new Vector<Need>(); 		//verwaltet die Bedürfnisse als abstrakte Objekte ohne konkretem Namen als Vektor
    private int	random_start;		    //Konstante: Startwert für Zufallszahlengenerator
    private int	random_end;		        //Konstante: Endwert für Zufallszahlengenerator
    private int	motivation_max_index;   //Der Index in der Liste, wo das Bedürfnis mit der höchsten Motivation steht
    private int	day_night_counter;	    //Zählvariable für den Tick
    private int	day_limit;		        //hier steht die Grenze, wie lange ein Tag dauern soll in Ticks; wird mit Tick_milsec im Konstruktor berechnet
    private int	night_limit;		    //hier steht die Grenze, wie lange eine Nacht dauern soll in Ticks; wird mit Tick_milsec im Konstruktor berechnet
    private boolean day_night;          //Zustandvariable, ob gerade Tag (1) oder Nacht (0) ist

    private final int day_time = 5*60;   //Ein Tag soll 5 Minuten dauern; Angabe ist in Sekunden
    private final int night_time = 1*60; //Eine Nacht soll 1 Minute dauern, Angabe ist in Sekunden


    //Standradkonstrktor - nimmt Werte, die sich als sinnvoll bzw logisch ergeben haben
    public KI() {

        //Initialisierung des Vectors
        init_vector();

        //Initialisierung privater Attribute mit eigens festgelegten Werten:
        init_attributes(1, 10, 100); //Erzeugung von Zufallszahlen zwischen 1 und 10; Tick alle 100ms
    }

    //überladener Konstrktor - für die Tests, um die KI verschieden zu konfigurieren
    public KI(int random_start, int random_end, int Tick_milsec){
        //Initialisierung des Vectors
        init_vector();

        //Initialisierung privater Attribute:
        init_attributes(random_start, random_end, Tick_milsec);
    }

     //Liest die .xml und füllt den Vektor mit Bedürfnissen
    private void init_vector(){
        int     top_level; 	    //Schwellenwert
        byte    priority; 	    //Priorität
        String  description;    //Beschreibung/ Name des Bedürfnises
        int     object_ID;	    //Objekt, welches das Bedürfnis braucht zum befrieedigen
        boolean day_night;	    //boolean für Tag (1) oder Nachtaktiv (0)

        /*ToDo:
            - .xml parsen
            - lokale Varibalen mit den Werten aus der xml belegen und für den Konstruktor der Bedürfnisse nutzen
            - Bedürfnisse beim Instanziieren in Vektor schieben

            Solange Eemete aus der xml gelesen werden können:
                lesen und lokale Variablen beschreiben
                Bedürfnis erzeugen und an Vektor anfügen

             needs_list.add( new Need(top_level, priority, description, object_ID, day_night));
         */
    }

    //Belegt die privaten Attribute mit Anfangswerten
    private void init_attributes(int lokal_random_start, int lokal_random_end, int lokal_Tick_milsec){
        motivation_max_index = 0;
        day_night_counter = 0;
        day_night = true;

        random_start = lokal_random_start;
        random_end   = lokal_random_end;
        day_limit    = day_time * 1000 / lokal_Tick_milsec;
        night_limit  = night_time * 1000 / lokal_Tick_milsec;

    }

    //Aufruf von Subjekt; Gibt Objekt_ID zurück, zu der das Subjekt als nächstes laufen soll
    public int getNextObject(){
        //ToDo: XMl parsen und XMPParser.getObjectbyID() aufrufen

        //Bedürfnis mit der höchsten Motivation resetten
        needs_list.elementAt(motivation_max_index).set_current_value(0);

        return needs_list.elementAt(motivation_max_index).get_object_id();
    }

    //um mit dem unfertigen Projekt kompilieren zu können, wird die Method enoch beibehalten
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

    //Algorithmus, in dem die KI den Vektor verwaltet und alle die Bedürfnisse neu berechnet
    public void tick() {
        int motivation_highest = 0;
        Need beduerfnis;

        //Index der höchsten Motivation zurücksetzen auf 0;
        motivation_max_index = 0;

        //feststellen, ob gerade Tag oder Nacht ist
        set_day_night();

        //Liste inkrementieren
        for(int i=0; i < needs_list.size(); i++) {
            //bedürfnis aus Liste nehmen
            beduerfnis = needs_list.elementAt(i);

            //Prüfen ob Tag oder Nacht ist und die entsprechenden Bedürfnisse nutzen
            if (day_night && beduerfnis.get_day_night()) { //es ist Tag und Bedürfnis ist tagaktiv
                motivation_highest = manage_list(i, motivation_highest);

            } else if (!day_night && !beduerfnis.get_day_night()) { //es ist Nacht und das Bedürfnis ist nachtaktiv
                motivation_highest = manage_list(i, motivation_highest);
            }
        }
    }

    //Zufallszahl zwischen random_start und random_end erzeugen
    private int get_int_random(){
        return random_start + (int) Math.round( Math.random() * (random_end - random_start) );
    }

    //Feststellen ob Tag oder Nacht ist
    private void set_day_night(){
        //Zählvariable für Tick inkrementieren
        day_night_counter++;

        //Zahlvariable zurücksetzen (day_night umschalten), wenn...
        if(day_night && day_night_counter >= day_limit){  //... Tag ist und die Zählvaribale das Tageslimit erreicht hat
            day_night_counter = 0;
            day_night = false;
        }else if(!day_night && day_night_counter >= night_limit){//... Nahct  ist und die Zählvariable das Nachtlimit erreicht aht
            day_night_counter = 0;
            day_night = true;
        }

        //sonst bleibt alles beim alten; das heißt: Tag/ NAcht geht weiter
    }

    //Werte des Bedürfnisses an der Stelle need_list[index] werden geändert und motivation_highest neu berechnet
    private int manage_list(int index, int motivation_highest){
        //es kann nicht das Objekt "beduerfnis" übergeben werden, da sich die eigenschfaten des Objektes ändern müssen. Dies soll dierekt in der Liste geschehen.

        //aktuellen Wert ändern; aktueller Wert = aktueller Wert + Zufallszahl
        needs_list.elementAt(index).set_current_value(needs_list.elementAt(index).get_current_value() + get_int_random());

        //Motivation neu berechnen; Motivation = Priorität * (aktueller Wert - Schwellwert)
        needs_list.elementAt(index).set_motivation(needs_list.elementAt(index).get_priority() * (needs_list.elementAt(index).get_current_value() - needs_list.elementAt(index).get_top_level()));

        //Höchte Motivation finden:
        //neuer index, wenn neue höchste Motivation
        if(needs_list.elementAt(index).get_motivation() > motivation_highest){
            motivation_max_index = index;
            motivation_highest = needs_list.elementAt(index).get_motivation();
            //neuer Index, wenn Motivationen übereinstimmen UND Priorität des neuen Bedürfnisses größer ist der Priorität des alten Bedürfnisses
        }else if(needs_list.elementAt(index).get_motivation() == motivation_highest && (needs_list.elementAt(index).get_priority() > needs_list.elementAt(motivation_max_index).get_priority())){
            motivation_max_index = index;
            motivation_highest = needs_list.elementAt(index).get_motivation();
        }

        return motivation_highest;
    }

}
