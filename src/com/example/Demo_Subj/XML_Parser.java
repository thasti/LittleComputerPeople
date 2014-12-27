package com.example.Demo_Subj;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//Klasse für das Parsen des XML-File, und befüllt die interne Baumstruktur
//über das Interface nutzen!
//InformationPublisher greift über das Interface auf diese Klasse zu
//->die Engine braucht nur den InformationPublisher und deren Funktionen aufrufen.
public class XML_Parser implements DataSource {
	private int id = 0;//fortlaufende interne id für die Baumstruktur
	private String platzhalter = "";//nur für Konsollenausgabe um die Ausgaben einzurücken
	private Tree welt;//Superparentinstanz welche die Liste mit allen childs enthält
	public boolean load_file(Object obj) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        File file;
        try {
            builder = factory.newDocumentBuilder();
            String typ = obj.getClass().toString().substring(obj.getClass().toString().lastIndexOf(".")+1);
            switch(typ){
                case "File":
                    file = (File)obj;
                    System.out.println("existiert Xml-file: "+file.exists());
                    if(file.exists()){
                        document = builder.parse(file);
                        load_childs(document,0);
                    }
                    else{
                        System.out.println("XML Datei konnte nicht geladen werden!");
                        return false;
                    }
                    break;
                case "AssetManager$AssetInputStream":
                    document = builder.parse((InputStream)obj);
                    load_childs(document,0);
                    break;
                case "String":
                    file = new File((String)obj);
                    document = builder.parse(file);
                    load_childs(document,0);
                    break;
                default:
                    System.out.println("Classtyp unbekannt: "+typ);
                    return false;
            }
        } catch (ParserConfigurationException e) {
            System.out.println("DocumentBuilder konnte nicht initialisiert werden");
            return false;
        } catch (SAXException | IOException e) {
            System.out.println("Fehler beim XML Parsen!");
            return false;
        }
		return true;

	}//lädt Datei und befüllt die "welt"(Instanz von Tree)
	private void load_childs(Node parent, int parent_id){
		System.out.println(platzhalter+"Parent:"+parent.getNodeName());
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
	}//wird innerhalb von load_file() iterativ aufgerufen, lädt alle Unterknoten vom parent
	private void load_attributes(Node child, int id){
		if(child.getAttributes().getLength()>0){
			int i = 0;
			while(i<child.getAttributes().getLength()){
				if(!child.getAttributes().item(i).toString().equals("#text")){
					//bei einem Zeilenumbruch equals("#text") == true
					System.out.println(platzhalter+child.getAttributes().item(i).getNodeName()+"="+child.getAttributes().item(i).getNodeValue());
					welt.add_parameter(child.getAttributes().item(i).getNodeName(), child.getAttributes().item(i).getNodeValue(),id);
				}
				i++;
			}
		}
	}//wird innerhalb von load_childs() aufgerufen um alle Paramter in der Liste des jeweiligen Elements eingetragen
	public void print_tree(){
		welt.print_all_childs(welt);
	}//Konsollenausgabe
    public XML_Parser(){
        //Welt initialisieren
        welt = new Tree("id","0");
        welt.add_parameter("typ", "welt",0);
    }
	public TreeNode get_super_parent() {
		if(welt.childs.size()<2){
			TreeNode tmp = new TreeNode();
			tmp.Nodes = welt.childs.get(0).parameter;
			tmp.Number = welt.string_to_int(welt.get_parameter_value(welt.childs.get(0), "id"));
			return tmp;
		}
		else
			return null;//es gibt kein einzelnes Superparent
	}
	public List<TreeNode> get_all_childs_from(TreeNode child) {
		List<TreeNode> list = new ArrayList<TreeNode>();
        if(child!=null) {
            Tree tmp = welt.get_child_with_id(child.Number);
            int i = 0;
            while (tmp.childs.size() > i) {
                TreeNode t = new TreeNode();
                t.Nodes = tmp.childs.get(i).parameter;
                t.Number = welt.string_to_int(welt.get_parameter_value(tmp.childs.get(i), "id"));
                list.add(t);
                i++;
            }
        }
		return list;
	}//Liste ist 0 wenn es keine childs hat
	public String get_value_with_key_from(TreeNode child, String key) {
		Tree tmp = welt.get_child_with_id(child.Number);
		return welt.get_parameter_value(tmp, key);
	}//gibt leeren String zurück wenn das child in den parametern den key nicht besitzt
	public TreeNode get_child_from(TreeNode child, String key, String value) {
		TreeNode t = new TreeNode();
		Tree tmp = welt.get_child_with_id(child.Number);
		if(tmp.get_child(key, value)!=null){
			tmp = tmp.get_child(key, value);
			t.Nodes = tmp.parameter;
			t.Number = welt.string_to_int(welt.get_parameter_value(tmp, "id"));
			return t;
		}
		return null;
	}//wenn es das child nicht gibt, kommt null zurück. Sucht nur in der oberesten Childebene des Elements
	public List<TreeNode> get_childs_with(String key, String value){
		//TreeNode tmp = new TreeNode();
		List<TreeNode> tmp_list = new ArrayList<TreeNode>();
		tmp_list = get_childs_with_iterativ(welt, key, value);
		return tmp_list;
	}//ruft get_childs_with_iterativ() iterativ auf um alle Childebenen abzufragen. Liste ist 0 wenn es keine Childs gibt
	private List<TreeNode> get_childs_with_iterativ(Tree parent, String key, String value){
		TreeNode tmp = new TreeNode();
		List<TreeNode> tmp_list = new ArrayList<TreeNode>();
		int i = 0;
		while(i<parent.childs.size()){
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
	}//nur für den iterativen aufruf nötig
	public TreeNode get_parent_from(TreeNode child) {
		TreeNode t = new TreeNode();
		Tree tmp = welt.get_child_with_id(child.Number);
		t.Number = welt.string_to_int(welt.get_parameter_value(tmp, "parentid"));
		t.Nodes = welt.get_child_with_id(t.Number).parameter;
		return t;
	}
}
