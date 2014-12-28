package com.example.Demo_Subj;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class C_XML_Parser implements I_XML_Parser{
	private int id = 0;
	private String platzhalter = "";
	private c_Tree welt;
	public boolean load_file(Object obj) {
		// TODO Automatisch generierter Methodenstub
		File file = new File("");// = new File("xml.txt");
		String typ = obj.getClass().toString().substring(obj.getClass().toString().lastIndexOf(".")+1);

		switch(typ){
			case "File":
                file = (File)obj;
				break;
			case "ArrayList":
				break;
			case "Integer":
				break;
			case "String":
				file = new File((String)obj);
				break;
			default:
				System.out.println("Classtyp unbekannt: "+typ);
				break;
		}
		System.out.println("existiert Xml-file: "+file.exists());
		if(file.exists()){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(file);
				load_childs(document,0);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Automatisch generierter Erfassungsblock
				System.out.println("Fehler beim XML Parsen!");
				return false;
			}
			return true;
		}
		else{
			System.out.println("XML Datei konnte nicht geladen werden!");
			return false;
		}
	}
	private void load_childs(Node parent, int parent_id){
		System.out.println(platzhalter+"Parent:"+parent.getNodeName());
		//welt.addChild(key, value, id, parent_id);
		platzhalter+="	";
		NodeList childs = parent.getChildNodes();
		int i = 0;
		while(i<childs.getLength()){
			if(!childs.item(i).getNodeName().toString().equals("#text")){
				//#text -> Zeilenumbruch
				System.out.println(platzhalter+"Childtyp: "+childs.item(i).getNodeName());
				id++;
				welt.add_child("typ",childs.item(i).getNodeName() , id, parent_id);
				load_attributes(childs.item(i),id);
				if(childs.item(i).hasChildNodes()){
						load_childs(childs.item(i),id);
				}
			}
			i++;
		}
		platzhalter=platzhalter.substring(1);
	}
	private void load_attributes(Node child, int id){
		if(child.getAttributes().getLength()>0){
			int i = 0;
			while(i<child.getAttributes().getLength()){
				if(!child.getAttributes().item(i).toString().equals("#text")){
					//#text bedeutet Zeilenumbruch
					System.out.println(platzhalter+child.getAttributes().item(i).getNodeName()+"="+child.getAttributes().item(i).getNodeValue());
					welt.add_parameter(child.getAttributes().item(i).getNodeName(), child.getAttributes().item(i).getNodeValue(),id);
				}
				i++;
			}
		}
	}
	public void print_tree(){
		welt.print_all_childs(welt);
		/*System.out.println(welt.getAllParameter());
		int i = 0;
		while(i<welt.childs.size()){
			System.out.println(welt.childs.get(i).getAllParameter());
			i++;
		}*/
	}
    public C_XML_Parser(){
        //Welt initialisieren
        welt = new c_Tree("id","0");
        welt.add_parameter("typ", "welt",0);
    }
	public c_xml_object get_super_parent() {
		if(welt.childs.size()<2){
			c_xml_object tmp = new c_xml_object();
			tmp.Nodes = welt.childs.get(0).parameter;
			tmp.Number = welt.string_to_int(welt.get_parameter_value(welt.childs.get(0), "id"));
			return tmp;
		}
		else
			return null;//es gibt kein einzelnes Superparent
	}
	public List<c_xml_object> get_all_childs_from(c_xml_object child) {
		List<c_xml_object> list = new ArrayList<c_xml_object>();
		c_Tree tmp = welt.get_child_with_id(child.Number);
		int i = 0;
		while(tmp.childs.size()>i){
			c_xml_object t = new c_xml_object();
			t.Nodes = tmp.childs.get(i).parameter;
			t.Number = welt.string_to_int(welt.get_parameter_value(tmp.childs.get(i),"id"));
			list.add(t);
			i++;
		}
		return list;
	}
	public String get_value_with_key_from(c_xml_object child, String key) {
		c_Tree tmp = welt.get_child_with_id(child.Number);
		return welt.get_parameter_value(tmp, key);
	}
	public c_xml_object get_child_from(c_xml_object child, String key, String value) {
		c_xml_object t = new c_xml_object();
		c_Tree tmp = welt.get_child_with_id(child.Number);
		if(tmp.get_child(key, value)!=null){
			tmp = tmp.get_child(key, value);
			t.Nodes = tmp.parameter;
			t.Number = welt.string_to_int(welt.get_parameter_value(tmp, "id"));
			return t;
		}
		return null;
	}
	public List<c_xml_object> get_childs_with(String key, String value){
		//c_xml_object tmp = new c_xml_object();
		List<c_xml_object> tmp_list = new ArrayList<c_xml_object>();
		tmp_list = get_childs_with_iterativ(welt, key, value);
		/*
		int i = 0;
		while(i<welt.childs.size()){
			if(welt.getParameterValue(welt.childs.get(i),key).compareTo(value)==0){
				tmp.Nodes = welt.childs.get(i).parameter;
				tmp.Number = welt.StringToInt(welt.getParameterValue(welt.childs.get(i), "id"));
				tmp_list.add(tmp);
			}
			if(welt.childs.size()>0){
				tmp_list.addAll(this.get_childs_with_iterativ(key,value));
			}
			i++;
		}*/
		return tmp_list;
	}
	private List<c_xml_object> get_childs_with_iterativ(c_Tree parent, String key, String value){
		c_xml_object tmp = new c_xml_object();
		List<c_xml_object> tmp_list = new ArrayList<c_xml_object>();
		int i = 0;
		while(i<parent.childs.size()){
			/*System.out.println("Key: "+key+" Value: "+value);
			System.out.println("welt: "+welt.parameter.entrySet());
			System.out.println("parent: "+parent.childs.get(i).parameter.entrySet());
			System.out.println("welt: "+welt.getChildId(key, value));*/
			if(welt.get_parameter_value(parent.childs.get(i), key).compareTo(value)==0){
				tmp.Nodes = parent.childs.get(i).parameter;
				tmp.Number = parent.string_to_int(parent.get_parameter_value(parent.childs.get(i), "id"));
				tmp_list.add(tmp);
			}
			if(parent.childs.size()>0){
				tmp_list.addAll(this.get_childs_with_iterativ(parent.childs.get(i),key,value));
			}
			i++;
		}
		return tmp_list;
	}
	public c_xml_object get_parent_from(c_xml_object child) {
		c_xml_object t = new c_xml_object();
		c_Tree tmp = welt.get_child_with_id(child.Number);
		t.Number = welt.string_to_int(welt.get_parameter_value(tmp, "parentid"));
		t.Nodes = welt.get_child_with_id(t.Number).parameter;
		return t;
	}
}
