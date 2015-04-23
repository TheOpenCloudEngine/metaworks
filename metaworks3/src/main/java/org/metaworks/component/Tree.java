package org.metaworks.component;

import java.util.ArrayList;

import org.metaworks.annotation.Id;

public class Tree {
	
	public final static String ALIGN_LEFT			= "left";
	public final static String ALIGN_RIGHT			= "right";
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	String align;
		public String getAlign() {
			return align;
		}
		public void setAlign(String align) {
			this.align = align;
		}

	boolean showCheckBox;
		public boolean isShowCheckBox() {
			return showCheckBox;
		}
		public void setShowCheckBox(boolean showCheckBox) {
			this.showCheckBox = showCheckBox;
		}
		
	boolean hiddenCheckBoxFolder;
		public boolean isHiddenCheckBoxFolder() {
			return hiddenCheckBoxFolder;
		}
		public void setHiddenCheckBoxFolder(boolean hiddenCheckBoxFolder) {
			this.hiddenCheckBoxFolder = hiddenCheckBoxFolder;
		}

	TreeNode node;
		public TreeNode getNode() {
			return node;
		}
		public void setNode(TreeNode node) {
			this.node = node;
		}
	
	ArrayList<TreeNode> checkNodes;
		public ArrayList<TreeNode> getCheckNodes() {
			return checkNodes;
		}
		public void setCheckNodes(ArrayList<TreeNode> checkNodes) {
			this.checkNodes = checkNodes;
		}
		
	TreeNode selectNode;
		public TreeNode getSelectNode() {
			return selectNode;
		}
		public void setSelectNode(TreeNode selectNode) {
			this.selectNode = selectNode;
		}
		
	public Tree() {
		this.setAlign(ALIGN_LEFT);		
	}
}
	
