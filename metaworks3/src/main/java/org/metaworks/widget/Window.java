package org.metaworks.widget;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;

@Face(ejsPath="genericfaces/Window.ejs", options={"hideLabels"}, values={"true"})
public class Window implements ContextAware {
	
	public Window() {
		this(null, "");
	}
	public Window(String title) {
		this(null, title);
	}	
	public Window(Object panel) {
		this(panel, "");
	}
	
	public Window(Object panel, String title) {		
		this.panel = panel;
		this.title = title;
	}
	
	Object panel;
		public Object getPanel() {
			return panel;
		}
		public void setPanel(Object panel) {
			this.panel = panel;
		}

	String title;
		@Hidden
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}		
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
}
