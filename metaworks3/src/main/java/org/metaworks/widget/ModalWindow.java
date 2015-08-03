package org.metaworks.widget;

import java.util.HashMap;
import java.util.Map;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;

import javax.xml.ws.Service;

@Face(displayName="ModalWindow")
public class ModalWindow implements ContextAware {
	
	public ModalWindow() {
		this(null, 400, 0, null);
	}
	
	public ModalWindow(Object panel) {
		this(panel, 400, 0, null);
	}
	
	public ModalWindow(Object panel, String title) {
		this(panel, 400, 0, title);
	}
	
	public ModalWindow(Object panel, int width, int height){
		this(panel, width, height, null);
	}
			
	public ModalWindow(Object panel, int width, int height, String title) {
		setMetaworksContext(new MetaworksContext());
		setButtons(new HashMap<String, Object>());
		setCallback(new HashMap<String, Object>());
		setPanel(panel);
		setWidth(width);
		setHeight(height);		
		setTitle(title);
		setOpen(true);
		setResizable(true);
	}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	String id;
		@Id
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	boolean open;
		@Hidden
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		
	String title;
		@Hidden
		public String getTitle() {
			return title;
		}
	
		public void setTitle(String title) {
			this.title = title;
		}

	int width;	
		@Hidden
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}

	int height;
		@Hidden
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
	
	boolean resizable;
		@Hidden
		public boolean isResizable() {
			return resizable;
		}
		public void setResizable(boolean resizable) {
			this.resizable = resizable;
		}
		
	Object panel;
		public Object getPanel() {
			return panel;
		}
		public void setPanel(Object panel) {
			this.panel = panel;
		}
		
	Map<String, Object> buttons;	
		@Hidden
		public Map<String, Object> getButtons() {
			return buttons;
		}
		public void setButtons(Map<String, Object> buttons) {
			this.buttons = buttons;
		}
		
	Map<String, Object> callback;
		@Hidden	
		public Map<String, Object> getCallback() {
			return callback;
		}
		public void setCallback(Map<String, Object> callback) {
			this.callback = callback;
		}

	@Hidden
	@ServiceMethod(eventBinding=EventContext.EVENT_CLOSE, bindingHidden=true)
	public Object close() {
		return new Remover(ServiceMethodContext.TARGET_SELF);
	}

	@Available(how="full-fledged")
	@ServiceMethod(target = ServiceMethod.TARGET_OPENER, callByContent = true)
	public Object ok(){

		MetaworksRemoteService.getInstance().wrapReturn(getPanel(), new Remover(new ModalWindow()));

		return getPanel();
	}

}
