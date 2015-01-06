package com.example.Demo_Subj;
import java.util.List;
import java.util.TreeMap;
public interface DataSource {
	boolean LoadFile(Object obj);
	TreeNode getSuperParent();
	List<TreeNode> getAllChildsFrom(TreeNode child);
	String getValueWithKeyFrom(TreeNode child, String key);
	TreeNode getChildFrom(TreeNode child, String key, String value);
	TreeNode getParentFrom(TreeNode child);
	List<TreeNode> getChildsWith(String key, String value);
}
class TreeNode {
	Integer Number = -1;//interne ID der Baumstruktur
	TreeMap<String, String> Nodes;//enthält alle Parameter des jeweiligen Elements
}