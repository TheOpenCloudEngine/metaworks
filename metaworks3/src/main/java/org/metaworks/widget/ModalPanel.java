package org.metaworks.widget;

import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class ModalPanel {
	
	Object value;
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
	public ModalPanel(){
		
	}
	
	public ModalPanel(Object value){
		this.setValue(value);
	}
	
}
