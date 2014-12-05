package com.example.Demo_Subj;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class c_Tree {
	TreeMap<String,String> parameter = new TreeMap<String, String>();//Parameter des Elements
	TreeMap<Integer,Integer> child_ids = new TreeMap<Integer,Integer>();//Key die ID, und Value der child-Index
	List <c_Tree> childs = new ArrayList<c_Tree>();
	public boolean add_child(String key, String value, Integer id, Integer parent_id){
		if((parameter.get("id").compareTo(parent_id.toString())==0)||(parent_id==0)){
			if(childs.isEmpty()){
				childs = new ArrayList<c_Tree>();
			}
			childs.add(new c_Tree(key,value));
			childs.get(childs.size()-1).parameter.put("id", id.toString());
			childs.get(childs.size()-1).parameter.put("parentid", parent_id.toString());
			child_ids.put(id,childs.size()-1);
			return true;
		}
		else{
			//eine Ebene herabsteigen
			if(child_ids.containsKey(parent_id)){
				//eins der Kinder hat die betreffende Parentid
				boolean ret = childs.get(child_ids.get(parent_id)).add_child(key, value, id, parent_id);
				return ret;
			}
			else{
                //Funktion iterativ ausführen um an die Kindes Kinder zu kommen
				int i = 0;
				while(i<childs.size()){
					boolean ret = childs.get(i).add_child(key, value, id, parent_id);
					if(ret==true){
						break;
					}
					i++;
				}
				return false;
			}
		}
	}
	public boolean equals_parentid_in_my_childs(Integer Parentid){
		return child_ids.equals(Parentid);
	}
	public boolean add_parameter(String key, String value, Integer id){
		if(parameter.get("id").compareTo(id.toString())==0){
			parameter.put(key, value);
			return true;
		}
		else{
			int i = 0;
			while(i<childs.size()){
				boolean ret = childs.get(i).add_parameter(key, value, id);
				if(ret==true){
					return true;
				}
				else{
					i++;
				}
			}
			return false;
		}	
	}
	public Set<String> get_all_parameter(c_Tree child){
		return child.parameter.keySet();
	}
	public String get_parameter_value(c_Tree child, String key){
		return child.parameter.get(key);
	}
	public c_Tree get_child(String key, String value){
		int index = 0;;
		int found_childs = 0;
		int i = 0;
		while(i<childs.size()){
			if(childs.get(i).parameter.get(key).equals(value)){
				found_childs++;
				index = i;
			}
			i++;
		}
		if(found_childs>1){
			return null;
		}
		else{
			return childs.get(index);
		}
		
	}
	public String get_child_id(String key, String value){
		int index = 0;;
		int found_childs = 0;
		int i = 0;
		while(i<childs.size()){
			if(childs.get(i).parameter.get(key).equals(value)){
				found_childs++;
				index = i;
			}
			i++;
		}
		if(found_childs>1){
			return null;
		}
		else{
			return childs.get(index).parameter.get("id");
		}
	}
	public void print_all_childs(c_Tree child_tmp){
		System.out.println(child_tmp.parameter.entrySet());
		int i = 0;
		while(i<childs.size()){
			child_tmp.childs.get(i).print_all_childs(childs.get(i));
			i++;
		}
	}
	public String get_parent_id(c_Tree child){
		return get_parameter_value(child,"id");
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
	c_Tree(String key, String value){
		parameter.put(key, value);
	}
}
