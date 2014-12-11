package com.example.Demo_Subj;
import java.util.TreeMap;
import java.util.List;
public class c_InformationPublisher {
	I_XML_Parser parser= new C_XML_Parser();
	boolean ret = false;
	class welt{
		TreeMap<Integer,Object> welt_object_list = new TreeMap<Integer,Object>();
		TreeMap<Integer,Object> welt_room_list = new TreeMap<Integer,Object>();
		TreeMap<Integer,Object> welt_house_list = new TreeMap<Integer,Object>();
	}
	welt welt = new welt();
	public int set_objectlist_for_one_room(Integer roomid){
		if(ret){
			//Liste ist in class welt enthalten
			List<c_xml_object> object_list;
			/*System.out.println(parser.get_super_parent().Nodes.values());
			System.out.println("childs: "+parser.get_all_childs_from(parser.get_super_parent()).size());
			System.out.println("child: "+parser.get_all_childs_from(parser.get_super_parent()).get(0).Nodes.entrySet());*/
			List<c_xml_object> room_list = parser.get_childs_with("roomID", roomid.toString());
			System.out.println("Roomlist.size: "+room_list.size()+" Object0 values: "+room_list.get(0).Nodes.entrySet());
			if(room_list.size()>1){
				System.out.println("RaumID mehrmals vergeben");
				return 0;
			}
			else{
				object_list = parser.get_all_childs_from(room_list.get(0));
			}
			int i = 0;
			while(i<object_list.size()){
				if(object_list.get(i).Nodes.containsKey("objectID")){
					welt.welt_object_list.put(string_to_int(object_list.get(i).Nodes.get("objectID")), new Object());
				}
				i++;
			}
			return 1;
		}
		return 0;
	}
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
	}
	public int set_room_list(Integer houseid){
		//Liste ist in class welt enthalten
		List<c_xml_object> room_list;
		if(ret){	
			if(houseid==null){
				//es gibt zur zeit nicht mehrere H�user
				c_xml_object superparent = parser.get_super_parent();
				room_list = parser.get_all_childs_from(superparent);
				while(room_list.get(0).Nodes.get("typ").compareTo("room")!=0){
					room_list = parser.get_all_childs_from(room_list.get(0));
					if(room_list.size()<1){
						System.out.println("typ room nicht gefunden");
						return 0;
					}
				}
			}
			else{
				List<c_xml_object> house_list = parser.get_childs_with("houseID", houseid.toString());
				if(house_list.size()>1){
					System.out.println("HausID mehrmals vergeben");
					return 0;
				}
				else{
					room_list = parser.get_all_childs_from(house_list.get(0));
				}
			}
			int i = 0;
			while(i<room_list.size()){
				if(room_list.get(i).Nodes.get("roomID")!=null){
					welt.welt_room_list.put(string_to_int(room_list.get(i).Nodes.get("roomID")), new Object());
				}
				i++;
			}
			return 1;
		}
		return 0;
	}
	public void set_house_list(){
		//Liste ist in class welt enthalten
		c_xml_object superparent = parser.get_super_parent();
		List<c_xml_object> house_list = parser.get_all_childs_from(superparent);
		//von Ebene zu Ebene nach unten hangeln, bis ich auf der haus-ebene bin
		//es gibt nur ein parent-container wo alle haeuser drin sind
		while(house_list.get(0).Nodes.get("typ").compareTo("house")!=0){
			house_list = parser.get_all_childs_from(house_list.get(0));
			if(house_list.size()<1){
				System.out.println("typ room nicht gefunden");
				break;
			}
		}
		//in house_list sollten jetzt alle haeuser gelistet sein
		//jetzt wird jedes haus mit den ben�tigten Werten in die Liste von Welt einsortiert
		int i = 0;
		while(i<house_list.size()){
			welt.welt_house_list.put(string_to_int(house_list.get(i).Nodes.get("houseID")), new Object());
			i++;
		}
	}
	public boolean can_use(){
		return ret;
	}
	public int string_to_int(String t){
		char[] a = t.toCharArray();
		int z=0,i=0;
		while(i<a.length){
			int exp = (int)Math.pow(10,a.length-i-1);
			if(exp ==0)
				exp = 1;
			//System.out.println((int)a[i]-48);
			z += (a[i]-48)*exp;
			i++;
		}
		return z;
	}
	c_InformationPublisher(){
		ret = parser.load_file("xml.txt");
	}
}
