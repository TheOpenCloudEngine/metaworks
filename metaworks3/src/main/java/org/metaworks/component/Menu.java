package org.metaworks.component;

import java.util.ArrayList;

import org.metaworks.annotation.Face;

@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class Menu {
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
	boolean context;
		public boolean isContext() {
			return context;
		}
		public void setContext(boolean context) {
			this.context = context;
		}

	boolean sub;
		public boolean isSub() {
			return sub;
		}
		public void setSub(boolean sub) {
			this.sub = sub;
		}

	ArrayList<MenuItem> menu;
		public ArrayList<MenuItem> getMenu() {
			return menu;
		}
		public void setMenu(ArrayList<MenuItem> menu) {
			this.menu = menu;
		}
	
	public Menu(){
		ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
		
		this.setMenu(menu);
	}
	
	public void add(MenuItem menuItem){
		this.getMenu().add(menuItem);
	}
}
