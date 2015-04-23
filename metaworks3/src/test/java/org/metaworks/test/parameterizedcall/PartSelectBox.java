package org.metaworks.test.parameterizedcall;

import java.util.ArrayList;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.SelectBox;

public class PartSelectBox extends SelectBox implements Face<Part>{

	@AutowiredFromClient 
	public AutowiredObject autowiredObject;
	
	@Override
	public void setValueToFace(Part value) {
		ArrayList values = new ArrayList();
		values.add(autowiredObject.getId());
		values.add(autowiredObject.getId());
		
		setOptionNames(values);
		setOptionValues(values);
		
		setSelectedValue(value.getId());
	}

	@Override
	public Part createValueFromFace() {
		Part part = new Part();
		part.setValue(getSelectedText());
		
		return part;
	}
	
	public void Face(){
		
	}

}
