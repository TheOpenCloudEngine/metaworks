package org.metaworks.widget;

import javax.validation.Valid;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Field;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;

@Face(ejsPath="dwr/metaworks/genericfaces/WindowFace.ejs", options={"hideLabels"}, values={"true"})
public class Popup implements ContextAware, ReturnWrapper {
	
	public Popup(){
		this(400,300, null);
	}

	public Popup(Object panel){		
		this(400,300, panel);
	}
	
	public Popup(int width, int height){
		this(width,height, null);
	}	
	
	public Popup(int width, int height, Object panel){
		setWidth(width);
		setHeight(height);
		setPanel(panel);
	}	

	Object panel;
		@Valid
		public Object getPanel() {
			return panel;
		}
		public void setPanel(Object panel) {
			this.panel = panel;
		}		
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}		

	String name;
		@Name
		@Hidden
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	int width;	
		@Field(descriptor="width")
		@Hidden
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}

	int height;
		@Hidden
		@Field(descriptor="height")
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}	
		
	@Hidden
	@ServiceMethod(eventBinding=EventContext.EVENT_CLOSE)
	public Object close() {
		return new Remover(ServiceMethodContext.TARGET_SELF);
	}

	@Override
	public void wrapReturnValue(Object object) {
		setPanel(object);
	}
}


