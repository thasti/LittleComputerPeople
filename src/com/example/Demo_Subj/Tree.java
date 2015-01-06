package com.example.Demo_Subj;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
//interne Klasse für XML_Parser -> stellt die Baumstruktur dar
public class Tree {
	TreeMap<String,String> parameter = new TreeMap<String, String>();//Parameter des Elements
	TreeMap<Integer,Integer> child_ids = new TreeMap<Integer,Integer>();//Key ist die fortlaufende interne ID, und Value der child-Index von der Liste "childs"
	List <Tree> childs = new ArrayList<Tree>();//Liste mit den Kindern des aktuellen Elements

    public boolean addChild(String key, String value, Integer id, Integer parent_id){
		if((parameter.get("id").compareTo(parent_id.toString())==0)||(parent_id==0)){
			if(childs.isEmpty()){
				childs = new ArrayList<Tree>();
			}
			childs.add(new Tree(key,value));
			childs.get(childs.size()-1).parameter.put("id", id.toString());
			childs.get(childs.size()-1).parameter.put("parentid", parent_id.toString());
			child_ids.put(id,childs.size()-1);
			return true;
		}
		else{
			//eine Ebene herabsteigen
			if(child_ids.containsKey(parent_id)){
				//eins der Kinder hat die betreffende Parentid
				return childs.get(child_ids.get(parent_id)).addChild(key, value, id, parent_id);
			}
			else{
                //Funktion iterativ ausführen um an die Kindes Kinder zu kommen
				int i = 0;
				while(i<childs.size()){
					boolean ret = childs.get(i).addChild(key, value, id, parent_id);
					if(ret==true){
						break;
					}
					i++;
				}
				return false;
			}
		}
	}
    /**********************************************************************************************
    child zur Liste hinzufügen
    **********************************************************************************************/

	public boolean addParameter(String key, String value, Integer id){
		if(parameter.get("id").compareTo(id.toString())==0){
			parameter.put(key, value);
			return true;
		}
		else{
			int i = 0;
			while(i<childs.size()){
				boolean ret = childs.get(i).addParameter(key, value, id);
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
    /**********************************************************************************************
    jedes Element besitzt eine Liste mit parametern aus der XML. zb.: Typ,name usw.
    **********************************************************************************************/

	public Set<String> getAllParameter(Tree child){
		return child.parameter.keySet();
	}

	public String getParameterValue(Tree child, String key){
		if(child.parameter.containsKey(key)){
			return child.parameter.get(key);
		}
		else
			return "";
	}

	public Tree getChild(String key, String value){
		//sucht nur innerhalb der obersten Childebene
		int index = 0;
		int found_childs = 0;
		int i = 0;
		while(i<childs.size()){
			if(childs.get(i).parameter.get(key).equals(value)){
				found_childs++;
				index = i;
			}
			i++;
		}
		if(found_childs==1){
            return childs.get(index);
		}
		else{
			return null;
		}
		
	}
    /**********************************************************************************************
    sucht nach einem Child mit einem bestimmten Key-Value. ACHTUNG! Sucht nur in der Liste der Childs von diesem Element, in keiner Ebene darunter!
    **********************************************************************************************/

    public Tree getChildWithId(Integer id){
		//id ist die interne id von der Baumstruktur
		if(child_ids.containsKey(id)){
			return childs.get(child_ids.get(id));
		}
		else{
			int i = 0;
			while(i<childs.size()){
				if(childs.get(i).getChildWithId(id)!=null)
					return childs.get(i).getChildWithId(id);
				else
					i++;
			}
			return null;
		}
	}
    /**********************************************************************************************
    die ID ist die, der internen Baumstruktur
    **********************************************************************************************/

    public String getChildId(String key, String value){
		//sucht nur innerhalb der obersten Childebene
		int index = 0;
		int found_childs = 0;
		int i = 0;
		while(i<childs.size()){
			if(childs.get(i).parameter.containsKey(key)){
				if(childs.get(i).parameter.get(key).equals(value)){
					found_childs++;
					index = i;
				}
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
    /**********************************************************************************************
	gibt die interne ID eines childs zurück. ACHTUNG! Sucht nur in der Liste der Childs von diesem Element, in keiner Ebene darunter!
    **********************************************************************************************/

    public void printAllChilds(Tree child_tmp){
		System.out.println(child_tmp.parameter.entrySet());
		int i = 0;
		while(i<childs.size()){
			child_tmp.childs.get(i).printAllChilds(childs.get(i));
			i++;
		}
	}
    /**********************************************************************************************
    Konsollenausgabe. Gibt alle Childs inklusive Unterebenen aus
    **********************************************************************************************/

    public String getParentId(Tree child){
		return getParameterValue(child, "parent_id");
	}
    /**********************************************************************************************
    gibt die parent-id aus der Parameterliste aus
    **********************************************************************************************/

	public int StringToInt(String t){
		/*char[] a = t.toCharArray();
		int z=0,i=0;
		while(i<a.length){
			int exp = (int)Math.pow(10,a.length-i-1);
			if(exp ==0)
				exp = 1;
			z += (a[i]-48)*exp;
			i++;
		}
		return z;*/
        return Integer.parseInt(t);
	}

	Tree(String key, String value){
		parameter.put(key, value);
	}
}
