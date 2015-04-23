package org.metaworks.test.faceclass;

import java.util.ArrayList;

import org.metaworks.Face;
import org.metaworks.component.SelectBox;

public class CompetencySelectBox extends SelectBox implements Face<Competency>{

	@Override
	public void setValueToFace(Competency value) {
		setSelectedValue(value.getName());
	}

	@Override
	public Competency createValueFromFace() {
		// TODO Auto-generated method stub
		return new Competency(getSelectedText());
	}
	
	public CompetencySelectBox(){
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
