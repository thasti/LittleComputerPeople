package com.example.Demo_Subj;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class C_XML_Parser{
	int id = 0;
	String platzhalter = "";
	c_Tree welt;

	public boolean load_file(Object obj) {
		// TODO Automatisch generierter Methodenstub
		File file = new File("xml.txt");
		System.out.println("existiert Xml-file: "+file.exists());
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
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			load_childs(document,0);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			System.out.println("Fehler beim XML Parsen!");
		}
		return false;
	}
	private void load_childs(Node parent, int parent_id){
		System.out.println(platzhalter+"Parent:"+parent.getNodeName());
		//welt.add_child(key, value, id, parent_id);
		platzhalter+="	";
		NodeList childs = parent.getChildNodes();
		int i = 0;
		while(i<childs.getLength()){
			if(!childs.item(i).getNodeName().toString().equals("#text")){
				//#text -> Zeilenumbruch
				System.out.println(platzhalter+"Childtyp: "+childs.item(i).getNodeName());
				id++;
				welt.add_child(childs.item(i).getNodeName(), childs.item(i).getNodeValue(), id, parent_id);
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
		/*System.out.println(welt.get_all_parameter());
		int i = 0;
		while(i<welt.childs.size()){
			System.out.println(welt.childs.get(i).get_all_parameter());
			i++;
		}*/
	}
    C_XML_Parser(){
        //Welt initialisieren
        welt = new c_Tree("id","0");
        welt.add_parameter("name", "welt",0);
    }
}
