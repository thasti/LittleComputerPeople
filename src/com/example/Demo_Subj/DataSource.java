package com.example.Demo_Subj;
import java.util.List;
import java.util.TreeMap;
public interface DataSource {
	boolean load_file(Object obj);
	TreeNode get_super_parent();
	List<TreeNode> get_all_childs_from(TreeNode child);
	String get_value_with_key_from(TreeNode child, String key);
	TreeNode get_child_from(TreeNode child, String key, String value);
	TreeNode get_parent_from(TreeNode child);
	List<TreeNode> get_childs_with(String key, String value);
}
class TreeNode {
	Integer Number = -1;//interne ID der Baumstruktur
	TreeMap<String, String> Nodes;//enthält alle Parameter des jeweiligen Elements
}