package com.example.Demo_Subj;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Environment;

import java.io.*;
import java.lang.reflect.Field;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

public class InformationPublisher extends ContextWrapper{
	DataSource parser= new XML_Parser();
	boolean ret = false;

    /*nur solange bis world global fertig ist*/
	class welt{
		TreeMap<Integer,item> welt_object_list = new TreeMap<Integer,item>();
		TreeMap<Integer,room> welt_room_list = new TreeMap<Integer,room>();
		TreeMap<Integer,Object> welt_house_list = new TreeMap<Integer,Object>();
        public void add_room(Integer id, room Room){
            this.welt_room_list.put(id, Room);
            System.out.println("Room der Liste hinzugefügt mit ID:"+id);
        }
        public void add_object(Integer id, item object){
            this.welt_object_list.put(id, object);
            System.out.println("Object der Liste hinzugefügt mit ID:"+id);
        }
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
    /*Testklasse für Raum*/
    class room{
        room(Integer ID, Integer bildresource, Double x, Double y, List<Integer>EnthalteneObjekte){
            System.out.println("Neuer Raum wird angelegt mit ID: "+ID+" Grafik: "+bildresource+" X: "+x+" Y:"+y+" enthaltene Objekte:");
            int i=0;
            while(i<EnthalteneObjekte.size()){
                System.out.println("ObjektID: "+EnthalteneObjekte.get(i));
                i++;
            }
        }
    }
    /*Testklasse für Objekte/items*/
    class item{
       item(Integer ID, Integer bildresource, Double x, Double y, String need, Integer sound, Integer popup, Integer user,List<String>BilderFuerAnimation){
           System.out.println("Neues Objekt wird angelegt mit ID: "+ID+" Grafik: "+bildresource+" X: "+x+" Y:"+y+" need:"+need+" sound:"+sound+" popup:"+popup+" user:"+user+" Animationsgrafiken:");
           int i=0;
           while(i<BilderFuerAnimation.size()){
               System.out.println("Grafik: "+BilderFuerAnimation.get(i));
               i++;
           }
       }
    }

	public List<Integer> set_objectlist_for_one_room(Integer roomid){
		if(ret){
			//Liste ist in class world enthalten
			List<TreeNode> object_list;
			List<TreeNode> room_list = parser.get_childs_with("roomID", roomid.toString());
			//System.out.println("Roomlist.size: "+room_list.size()+" Object0 values: "+room_list.get(0).Nodes.entrySet());
			if(room_list.size()>1){
				System.out.println("RaumID mehrmals vergeben");
				return null;
			}
			else{
				object_list = parser.get_all_childs_from(room_list.get(0));
			}
            List<Integer>ObjectIds = new ArrayList<Integer>();
			int i = 0;
			while(i<object_list.size()){
				if(object_list.get(i).Nodes.containsKey("objectID")){
                    ObjectIds.add(string_to_int(object_list.get(i).Nodes.get("objectID")));
                    List<String>animationImages = new ArrayList<String>();
                    List<TreeNode>animationImagesList = parser.get_all_childs_from(parser.get_child_from(object_list.get(i),"name","Animation"));
                    int m = 0;
                    while(m<animationImagesList.size()) {
                        animationImages.add(animationImagesList.get(m).Nodes.get("name"));
                        m++;
                    }
                    Integer ResId = ResourceNameToInt(object_list.get(i).Nodes.get("name"));
                    item it_tmp = new item(string_to_int(object_list.get(i).Nodes.get("objectID")),ResId,string_to_double(object_list.get(i).Nodes.get("x-position")),string_to_double(object_list.get(i).Nodes.get("y-position")),object_list.get(i).Nodes.get("need"),string_to_int(object_list.get(i).Nodes.get("sound")),string_to_int(object_list.get(i).Nodes.get("popup")),string_to_int(object_list.get(i).Nodes.get("user")),animationImages);
					welt.add_object(string_to_int(object_list.get(i).Nodes.get("objectID")), it_tmp);
				}
				i++;
			}
            return ObjectIds;
		}
		return null;
	}//fügt in globale objectliste alle Objekte ein die im raum enthalten sind
	// key der globalen Objectlist ist die ID aus der XML vom Object und value das Objekt/item selbst
    //eine Liste mit den ObjectIDs aus der XML wird zurückgegeben
    //Rückgabe von null bei einem Fehler!
	public List<Integer> set_room_list(Integer houseid){
		//Liste ist in class welt enthalten
		List<TreeNode> room_list;
        List<Integer> roomId_list = new ArrayList<Integer>();
		if(ret){	
			if(houseid==null){
				//es gibt zur zeit nicht mehrere H�user
				TreeNode superparent = parser.get_super_parent();
				room_list = parser.get_all_childs_from(superparent);
				while(room_list.get(0).Nodes.get("typ").compareTo("room")!=0){
					room_list = parser.get_all_childs_from(room_list.get(0));
					if(room_list.size()<1){
						System.out.println("typ room nicht gefunden");
						return null;
					}
				}
			}
			else{
				List<TreeNode> house_list = parser.get_childs_with("houseID", houseid.toString());
				if(house_list.size()>1){
					System.out.println("HausID mehrmals vergeben");
					return null;
				}
				else{
					room_list = parser.get_all_childs_from(house_list.get(0));
				}
			}
			int i = 0;
			while(i<room_list.size()){
				if(room_list.get(i).Nodes.get("roomID")!=null){
                    List<Integer> object_list = set_objectlist_for_one_room(string_to_int(room_list.get(i).Nodes.get("roomID")));
                    Integer ResId = ResourceNameToInt(room_list.get(i).Nodes.get("name"));
                    room room_tmp= new room(string_to_int(room_list.get(i).Nodes.get("roomID")),ResId,string_to_double(room_list.get(i).Nodes.get("x-position")),string_to_double(room_list.get(i).Nodes.get("y-position")),object_list);
                    roomId_list.add(string_to_int(room_list.get(i).Nodes.get("roomID")));
                    welt.add_room(string_to_int(room_list.get(i).Nodes.get("roomID")), room_tmp);
				}
				i++;
			}
			return roomId_list;
		}
		return null;
	}//befüllt die globale roomlist.
	// houseid = null übergeben wenn es keine häuser im xml-file gibt
    //ruft für jeden room den er findet set_objectlist_for_one_room() auf
    //->die globale objectliste wird automatisch mit befüllt
    //Rückgabe von null bei Fehler, sonst eine Liste mit den IDs der Räume aus der XML
	public List<Integer> set_house_list(){
		//Liste ist in class welt enthalten
		TreeNode superparent = parser.get_super_parent();
		List<TreeNode> house_list = parser.get_all_childs_from(superparent);
        List<Integer> houseId_list = new ArrayList<Integer>();
        if(ret) {
            //von Ebene zu Ebene nach unten hangeln, bis ich auf der haus-ebene bin
            //es gibt nur ein parent-container wo alle haeuser drin sind
            while (house_list.get(0).Nodes.get("typ").compareTo("house") != 0) {
                house_list = parser.get_all_childs_from(house_list.get(0));
                if (house_list.size() < 1) {
                    System.out.println("typ house nicht gefunden");
                    return null;
                }
            }
            //in house_list sollten jetzt alle haeuser gelistet sein
            //jetzt wird jedes haus mit den ben�tigten Werten in die Liste von Welt einsortiert
            int i = 0;
            while (i < house_list.size()) {
                List<Integer> roomIdList = set_room_list(string_to_int(house_list.get(i).Nodes.get("houseID")));
                house house_tmp = new house(string_to_int(house_list.get(i).Nodes.get("houseID")), house_list.get(i).Nodes.get("grafik"), roomIdList);
                houseId_list.add(string_to_int(house_list.get(i).Nodes.get("houseID")));
                welt.add_house(string_to_int(house_list.get(i).Nodes.get("houseID")), house_tmp);
                i++;
            }
            return houseId_list;
        }
        return null;
	}//befüllt die globale houselist.
    //ruft für jedes house welches er findet set_room_list() auf
    //->die globale raum und objectliste wird automatisch mit befüllt
    //Rückgabe von null bei Fehler, sonst eine Liste mit den IDs der Häuser aus der XML
    public int set_objectlist(){
        if(ret && welt.welt_room_list.size()>0){
            int i = 0;
            Integer key = welt.welt_room_list.firstKey();
            System.out.println("key: "+key);
            while(i<welt.welt_room_list.size()){
                set_objectlist_for_one_room(key);
                key = welt.welt_room_list.higherKey(key);
                i++;
            }
            return 1;
        }
        return 0;
    }//aufruf entfällt, wenn set_room_list() aufgerufen wird
    // befüllt die globale Objektliste
    // ruft iterativ set_objectlist_for_one_room() auf.
    //rückgabe 0 bei Fehler, sonst 1
	public boolean can_use(){
		return ret;
	}//konnte der DataSource instanziert werden? wenn das XML-File nicht geladen werden konnte bleibt ret auf false!
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
    public Integer string_to_int(String t){
        if(t!=null) {
            char[] a = t.toCharArray();
            int z = 0, i = 0;
            while (i < a.length) {
                int exp = (int) Math.pow(10, a.length - i - 1);
                if (exp == 0)
                    exp = 1;
                z += (a[i] - 48) * exp;
                i++;
            }
            return z;
        }
        return null;
	}
    public Double string_to_double(String t){
        if(t!=null) {
            char[] a = t.toCharArray();
            int i = 0, vk;
            double m = 0;
            while ((i < a.length) && (a[i] != '.')) {
                i++;
            }
            vk = i;
            i = 0;
            while (i < vk) {
                int exp = (int) Math.pow(10, vk - i - 1);
                if (exp == 0)
                    exp = 1;
                //System.out.println((int) a[i] - 48);
                m += (a[i] - 48) * exp;
                i++;
            }
            i++;
            while (i < a.length) {
                double exp = Math.pow(0.1, i - vk);
                //System.out.println((int) a[i] - 48);
                m += (a[i] - 48) * exp;
                i++;
            }
            return m;
        }
        return null;
    }
    public InformationPublisher(Context base, String file) {
        super(base);
        try {
            InputStream is = getAssets().open(file);
            ret = parser.load_file(is);
            /*InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s;
            while((s=br.readLine())!=null){
                System.out.println(s);
            }*/
        } catch (IOException e) {
            System.out.println("Fehler beim init InformationPublisher");
        }
	}//benötigt als Parameter eine Instanz einer Klasse welche von Context abgeleitet ist,
    //um auf das Verzeichnis assets im Projekt zugreifen zu können
    //in assets müssen die zu parsenden Dateien liegen und deren Name diesem Konstruktor als String übergeben werden
    //Klasse RoomActivity ist von Activity abgeleitet welche alle Funktionen von Context enthält
    //Verzeichnis assets beinhaltet laut Definition alle Dateien die nicht mit einem Standarddienst von Android geparset werden sollen/können
    //für den XML-Parser wird ein File, String mit dem Pfad zum File oder ein InputStream benötigt
    //in diesem Konstruktor gibt die benötigte Funktion getAssets().open() einen InputStream zurück
    //Über Ressource vom Verzeichnis res/raw kann man sich auch ein InputStream zurückgeben lassen, mit diesem funktioniert es allerdings noch nicht->
    //deswegen gehe ich zur Zeit den Umweg über das assets Verzeichnis der app.
}
