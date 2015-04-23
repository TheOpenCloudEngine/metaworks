package org.metaworks.widget;

import org.metaworks.annotation.Id;

public class Tab {

	public Tab() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String tabTitle;	
		@Id
		public String getTabTitle() {
			return tabTitle;
		}
		public void setTabTitle(String tabTitle) {
			this.tabTitle = tabTitle;
		}
	
	public Object tabContent;		
		public Object getTabContent() {
			return tabContent;
		}
		public void setTabContent(Object tabContent) {
			this.tabContent = tabContent;
		}
	
	public Tab(String title) {
		this.tabTitle = title;
		this.tabContent = new Object();
	}
	
	public Tab(String title,Object obj) {
		this.tabTitle = title;
		this.tabContent = obj;
	}
	
}
