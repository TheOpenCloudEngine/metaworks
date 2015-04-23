package org.metaworks.widget.chart;

import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;

@Face(ejsPathMappingByContext={
    	"{when: 'new', face: 'genericfaces/ObjectFace.ejs'}",
		"{when: 'edit', face: 'genericfaces/ObjectFace.ejs'}"
})
public class Guage implements ContextAware{
	

	String label;
		
		public String getLabel() {
			return label;
		}
	
		public void setLabel(String label) {
			this.label = label;
		}

	int value;
	
		public int getValue() {
			return value;
		}
	
		public void setValue(int value) {
			this.value = value;
		}
	
	@ServiceMethod(callByContent = true)
	public void show(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("show");
	}

	
	MetaworksContext metaworksContext;

		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}	
}
