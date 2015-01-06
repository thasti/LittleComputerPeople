package com.example.Demo_Subj;
import android.content.Context;
import android.content.ContextWrapper;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

public class InformationPublisher extends ContextWrapper{
    Context ctx;
	DataSource parser= new XML_Parser();
	boolean ret = false;

    /*nur solange bis world global fertig ist*/
	class welt{
		TreeMap<Integer,Object> welt_house_list = new TreeMap<Integer,Object>();
        public void add_house(Integer id, Object object){
            this.welt_house_list.put(id, object);
            System.out.println("House der Liste hinzugefügt mit ID:"+id);
        }
	}
	welt welt = new welt();

    /*Testklasse für Haus*/
    class house{
        house(Integer ID, String bildname, List<Integer>EnthalteneRaeume){
            System.out.println("Neues Haus wird angelegt mit ID: "+ID+" Grafik: "+bildname+" enthaltene Räume:");
            int i=0;
            while(i<EnthalteneRaeume.size()){
                System.out.println("RaumID: "+EnthalteneRaeume.get(i));
                i++;
            }
        }
    }

	public List<Integer> setObjectlistForOneRoom(Integer roomid){
		if(ret){
			//Liste ist in class world enthalten
			List<TreeNode> object_list;
			List<TreeNode> room_list = parser.getChildsWith("roomID", roomid.toString());
			//System.out.println("Roomlist.size: "+room_list.size()+" Object0 values: "+room_list.get(0).Nodes.entrySet());
			if(room_list.size()>1){
				System.out.println("RaumID mehrmals vergeben");
				return null;
			}
			else{
				object_list = parser.getAllChildsFrom(room_list.get(0));
			}
            List<Integer>ObjectIds = new ArrayList<Integer>();
			int i = 0;
			while(i<object_list.size()){
				if(object_list.get(i).Nodes.containsKey("objectID")){
                    ObjectIds.add(StringToInt(object_list.get(i).Nodes.get("objectID")));
                    List<String>animationImages = new ArrayList<String>();
                    List<TreeNode>animationImagesList = parser.getAllChildsFrom(parser.getChildFrom(object_list.get(i), "name", "Animation"));
                    int m = 0;
                    while(m<animationImagesList.size()) {
                        animationImages.add(animationImagesList.get(m).Nodes.get("name"));
                        m++;
                    }
                    Integer ResId = ResourceNameToInt(object_list.get(i).Nodes.get("name"));
                    Item it_tmp = new Item(StringToInt(object_list.get(i).Nodes.get("objectID")),ResId, GlobalInformation.getScreenWidth()*StringToDouble(object_list.get(i).Nodes.get("x-position")), GlobalInformation.getScreenHeight()*StringToDouble(object_list.get(i).Nodes.get("y-position")),object_list.get(i).Nodes.get("need"), StringToInt(object_list.get(i).Nodes.get("sound")), StringToInt(object_list.get(i).Nodes.get("popup")), StringToInt(object_list.get(i).Nodes.get("user")),animationImages,ctx);
					World.setItem(StringToInt(object_list.get(i).Nodes.get("objectID")), it_tmp);
				}
				i++;
			}
            return ObjectIds;
		}
		return null;
	}
    /**********************************************************************************************
    fügt in globale objectliste alle Objekte ein die im raum enthalten sind
	key der globalen Objectlist ist die ID aus der XML vom Object und value das Objekt/item selbst
    eine Liste mit den ObjectIDs aus der XML wird zurückgegeben
    Rückgabe von null bei einem Fehler!
    **********************************************************************************************/

    public List<Integer> setRoomlist(Integer houseid){
		//Liste ist in class welt enthalten
		List<TreeNode> room_list;
        List<Integer> roomId_list = new ArrayList<Integer>();
		if(ret){	
			if(houseid==null){
				//es gibt zur zeit nicht mehrere H�user
				TreeNode superparent = parser.getSuperParent();
				room_list = parser.getAllChildsFrom(superparent);
				while(room_list.get(0).Nodes.get("typ").compareTo("room")!=0){
					room_list = parser.getAllChildsFrom(room_list.get(0));
					if(room_list.size()<1){
						System.out.println("typ room nicht gefunden");
						return null;
					}
				}
			}
			else{
				List<TreeNode> house_list = parser.getChildsWith("houseID", houseid.toString());
				if(house_list.size()>1){
					System.out.println("HausID mehrmals vergeben");
					return null;
				}
				else{
					room_list = parser.getAllChildsFrom(house_list.get(0));
				}
			}
			int i = 0;
			while(i<room_list.size()){
				if(room_list.get(i).Nodes.get("roomID")!=null){
                    List<Integer> object_list = setObjectlistForOneRoom(StringToInt(room_list.get(i).Nodes.get("roomID")));
                    Integer ResId = ResourceNameToInt(room_list.get(i).Nodes.get("name"));
                    Room room_tmp= new Room(StringToInt(room_list.get(i).Nodes.get("roomID")),ResId, StringToDouble(room_list.get(i).Nodes.get("x-position")), StringToDouble(room_list.get(i).Nodes.get("y-position")),object_list,ctx);

                    /*fuer Raumnavigation die jeweils benachbarten Raeume einstellen*/
                    /*Festlegung, alles erstmal nur nen Schlauch*/
                    int left_room,right_room;
                    if(i==0)
                        left_room=-1;
                    else
                        left_room = StringToInt(room_list.get(i-1).Nodes.get("roomID"));
                    if(i==(room_list.size()-1))
                        right_room=-1;
                    else
                        right_room = StringToInt(room_list.get(i+1).Nodes.get("roomID"));
                    room_tmp.setAttachedRooms(left_room,right_room,-1,-1);
                    /*Ende*/
                    /*Hier muessen spaeter die Informationen aus der XML geholt werden*/

                    roomId_list.add(StringToInt(room_list.get(i).Nodes.get("roomID")));
                    World.setRoom(StringToInt(room_list.get(i).Nodes.get("roomID")), room_tmp);

				}
				i++;
			}
			return roomId_list;
		}
		return null;
	}
    /**********************************************************************************************
	befüllt die globale roomlist.
	houseid = null übergeben wenn es keine häuser im xml-file gibt
    ruft für jeden room den er findet setObjectlistForOneRoom() auf
    ->die globale objectliste wird automatisch mit befüllt
    Rückgabe von null bei Fehler, sonst eine Liste mit den IDs der Räume aus der XML
    **********************************************************************************************/

    public List<Integer> setHouselist(){
		//Liste ist in class welt enthalten
		TreeNode superparent = parser.getSuperParent();
		List<TreeNode> house_list = parser.getAllChildsFrom(superparent);
        List<Integer> houseId_list = new ArrayList<Integer>();
        if(ret) {
            //von Ebene zu Ebene nach unten hangeln, bis ich auf der haus-ebene bin
            //es gibt nur ein parent-container wo alle haeuser drin sind
            while (house_list.get(0).Nodes.get("typ").compareTo("house") != 0) {
                house_list = parser.getAllChildsFrom(house_list.get(0));
                if (house_list.size() < 1) {
                    System.out.println("typ house nicht gefunden");
                    return null;
                }
            }
            //in house_list sollten jetzt alle haeuser gelistet sein
            //jetzt wird jedes haus mit den ben�tigten Werten in die Liste von Welt einsortiert
            int i = 0;
            while (i < house_list.size()) {
                List<Integer> roomIdList = setRoomlist(StringToInt(house_list.get(i).Nodes.get("houseID")));
                house house_tmp = new house(StringToInt(house_list.get(i).Nodes.get("houseID")), house_list.get(i).Nodes.get("grafik"), roomIdList);
                houseId_list.add(StringToInt(house_list.get(i).Nodes.get("houseID")));
                welt.add_house(StringToInt(house_list.get(i).Nodes.get("houseID")), house_tmp);
                i++;
            }
            return houseId_list;
        }
        return null;
	}
    /**********************************************************************************************
    befüllt die globale houselist.
    ruft für jedes house welches er findet setRoomlist() auf
    ->die globale raum und objectliste wird automatisch mit befüllt
    Rückgabe von null bei Fehler, sonst eine Liste mit den IDs der Häuser aus der XML
    **********************************************************************************************/

    public int setObjectlist(){
        if(ret && World.getRoomlistSize()>0){
            int i = 0;
            Integer key = World.getRoomlistFirstKey();
            System.out.println("key: "+key);
            while(i<World.getRoomlistSize()){
                setObjectlistForOneRoom(key);
                key = World.getRoomlistHigherKey(key);
                i++;
            }
            return 1;
        }
        return 0;
    }
    /**********************************************************************************************
    Aufruf entfällt, wenn setRoomlist() aufgerufen wird
    befüllt die globale Objektliste
    ruft iterativ setObjectlistForOneRoom() auf.
    rückgabe 0 bei Fehler, sonst 1
    **********************************************************************************************/

    public boolean can_use(){
		return ret;
	}
    /**********************************************************************************************
    konnte der DataSource instanziert werden? wenn das XML-File nicht geladen werden konnte bleibt ret auf false!
    **********************************************************************************************/

    public Integer ResourceNameToInt(String name){
        if(name!=null) {
            try {
                Field f = R.drawable.class.getDeclaredField(name);
                return f.getInt(f);

            } catch (IllegalAccessException e) {
                System.out.println("Fehler beim beziehen der ResourceId");
            } catch (NoSuchFieldException e) {
                System.out.println("Fehler beim beziehen der ResourceId");
            }
        }
        return null;
    }

    public Integer StringToInt(String t){
        if(t!=null) {
            return Integer.parseInt(t);
        }
        return null;
	}

    public Double StringToDouble(String t){
        if(t!=null) {
            return Double.parseDouble(t);
        }
        return null;
    }

    public List<Need> getNeedsFromXml(String file){
        DataSource NeedsParser= new XML_Parser();
        InputStream is = null;
        try {
            is = getAssets().open(file);
            NeedsParser.LoadFile(is);
            TreeNode allneeds = NeedsParser.getSuperParent();
            List<TreeNode> NeedsListTmp = NeedsParser.getAllChildsFrom(allneeds);
            List<Need> NeedsList = new ArrayList();
            int i = 0;
            while(i<NeedsList.size()) {
                boolean activeDayNight;
                if(StringToInt(NeedsListTmp.get(i).Nodes.get("daynight")) == 1) {
                    activeDayNight = true;
                }
                else {
                    activeDayNight = false;
                }
                NeedsList.add(new Need(StringToInt(NeedsListTmp.get(i).Nodes.get("toplevel")),
                        StringToInt(NeedsListTmp.get(i).Nodes.get("priority")).byteValue(),
                        NeedsListTmp.get(i).Nodes.get("name"),
                        StringToInt(NeedsListTmp.get(i).Nodes.get("objectID")),
                        activeDayNight));
                i++;
            }
            return NeedsList;
        } catch (IOException e) {
            System.out.println("Fehler beim NeedsXML parsen!");
        }
        return null;
    }
    /**********************************************************************************************
    liest XML-File need aus
     */

    public InformationPublisher(Context base, String file) {
        super(base);
        ctx = base;
        try {
            InputStream is = getAssets().open(file);
            ret = parser.LoadFile(is);
            /*InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s;
            while((s=br.readLine())!=null){
                System.out.println(s);
            }*/
        } catch (IOException e) {
            System.out.println("Fehler beim init InformationPublisher");
        }
	}
    /**********************************************************************************************
    benötigt als Parameter eine Instanz einer Klasse welche von Context abgeleitet ist,
    um auf das Verzeichnis assets im Projekt zugreifen zu können
    in assets müssen die zu parsenden Dateien liegen und deren Name diesem Konstruktor als String übergeben werden
    Klasse RoomActivity ist von Activity abgeleitet welche alle Funktionen von Context enthält
    Verzeichnis assets beinhaltet laut Definition alle Dateien die nicht mit einem Standarddienst von Android geparset werden sollen/können
    für den XML-Parser wird ein File, String mit dem Pfad zum File oder ein InputStream benötigt
    in diesem Konstruktor gibt die benötigte Funktion getAssets().open() einen InputStream zurück
    Über Ressource vom Verzeichnis res/raw kann man sich auch ein InputStream zurückgeben lassen, mit diesem funktioniert es allerdings noch nicht->
    deswegen gehe ich zur Zeit den Umweg über das assets Verzeichnis der app.
    **********************************************************************************************/
}
