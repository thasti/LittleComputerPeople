package com.example.Demo_Subj;
import java.util.List;
import java.util.TreeMap;
public interface I_XML_Parser {
	boolean load_file(Object obj);
	c_xml_object get_super_parent();
	List<c_xml_object> get_all_childs_from(c_xml_object child);
	String get_value_with_key_from(c_xml_object child, String key);
	c_xml_object get_child_from(c_xml_object child, String key, String value);
	c_xml_object get_parent_from(c_xml_object child);
	List<c_xml_object> get_childs_with(String key, String value);
}
class c_xml_object{
	Integer Number = -1;//intern
	TreeMap<String, String> Nodes;
}