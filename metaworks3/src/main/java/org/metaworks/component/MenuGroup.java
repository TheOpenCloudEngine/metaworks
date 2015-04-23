package org.metaworks.component;

import java.util.ArrayList;

import org.metaworks.annotation.Face;

@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class MenuGroup {

	ArrayList<Menu> group;
		public ArrayList<Menu> getGroup() {
			return group;
		}
		public void setGroup(ArrayList<Menu> group) {
			this.group = group;
		}

	public MenuGroup() {
		ArrayList<Menu> group = new ArrayList<Menu>();
		
		this.setGroup(group);
	}
	
	public void add(Menu menu) {
		this.getGroup().add(menu);
	}
}
