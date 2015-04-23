package org.metaworks.component;

import java.util.ArrayList;

import org.metaworks.annotation.Face;

@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class MenuGroups {
	ArrayList<MenuGroup> groups;
		public ArrayList<MenuGroup> getGroups() {
			return groups;
		}
		public void setGroups(ArrayList<MenuGroup> groups) {
			this.groups = groups;
		}
		
	public MenuGroups(){
		ArrayList<MenuGroup> groups = new ArrayList<MenuGroup>();
		
		this.setGroups(groups);
	}
	
	public void add(MenuGroup group){
		this.getGroups().add(group);
	}
}
