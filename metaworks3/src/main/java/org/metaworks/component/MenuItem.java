package org.metaworks.component;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;

@Face(ejsPathForArray="genericfaces/CleanArrayFace.ejs")
public class MenuItem {
	
	public final static String TYPE_MENUITEM = "menu_item";
	public final static String TYPE_DIVIDER = "menu_divider";
	
	String id;
		@Id
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
		
	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	String shortcut;
		public String getShortcut() {
			return shortcut;
		}
		public void setShortcut(String shortcut) {
			this.shortcut = shortcut;
		}
		
	Menu subMenu;
		public Menu getSubMenu() {
			return subMenu;
		}
		public void setSubMenu(Menu subMenu) {
			this.subMenu = subMenu;
		}
		
	public MenuItem(){
		this(TYPE_MENUITEM);
	}
	
	public MenuItem(String type){
		this.setType(type);
	}
	
	public MenuItem(String id, String name){
		this();
		this.setId(id);
		this.setName(name);
	}
	
}
