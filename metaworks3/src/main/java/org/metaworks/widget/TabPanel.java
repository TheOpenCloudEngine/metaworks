package org.metaworks.widget;

import java.util.ArrayList;
import java.util.List;

public class TabPanel {

	private List tabs;
	
	public List getTabs() {
		return tabs;
	}

	public void setTabs(List tabs) {
		this.tabs = tabs;
	}

	public TabPanel() {
		tabs = new ArrayList();
	}
	
	public void addTab(Tab obj) {
		tabs.add(obj);
	}
	
	public void removeTab(Tab obj) {
		tabs.remove(obj);
	}
}
