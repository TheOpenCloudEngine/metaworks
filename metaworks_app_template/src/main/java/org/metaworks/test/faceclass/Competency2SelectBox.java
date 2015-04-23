package org.metaworks.test.faceclass;

import java.util.ArrayList;

import org.metaworks.Face;
import org.metaworks.component.SelectBox;

public class Competency2SelectBox extends SelectBox implements Face<Competency2>{

	@Override
	public void setValueToFace(Competency2 value) {
		setSelectedValue(value.getName());
	}

	@Override
	public Competency2 createValueFromFace() {
		// TODO Auto-generated method stub
		return new Competency2(getSelectedText());
	}
	
	public Competency2SelectBox(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("1");
		options.add("2");
		ArrayList<String> values = new ArrayList<String>();
		values.add("1");
		values.add("2");
		
		setOptionNames(options);
		setOptionValues(values);
	}

}
