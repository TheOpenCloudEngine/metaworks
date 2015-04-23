package org.metaworks.widget;

import java.util.ArrayList;

import org.metaworks.annotation.Id;

public class Choice {
	public Choice() {
		setOptionValues(new ArrayList<String>());
		setOptionNames(new ArrayList<String>());
	}
	
	String id;	
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	ArrayList<String> optionValues;
		public ArrayList<String> getOptionValues() {
			return optionValues;
		}
		public void setOptionValues(ArrayList<String> optionValues) {
			this.optionValues = optionValues;
		}
		
	ArrayList<String> optionNames;
		public ArrayList<String> getOptionNames() {
			return optionNames;
		}
		public void setOptionNames(ArrayList<String> optionNames) {
			this.optionNames = optionNames;
		}
	
	String selected;
		public String getSelected() {
			return selected;
		}
		public void setSelected(String selected) {
			this.selected = selected;
		}
		
	String selectedText;
		public String getSelectedText() {
			return selectedText;
		}
		public void setSelectedText(String selectedText) {
			this.selectedText = selectedText;
		}
	
	public void add(String name, String value){
		getOptionValues().add(value);
		getOptionNames().add(name);		
	}
	
	public void add(int i, String name, String value){
		getOptionValues().add(i, value);
		getOptionNames().add(i, name);
	}
	
	public void select(int index) {
		setSelected(getOptionValues().get(index));
		setSelectedText(getOptionNames().get(index));
	}
	
	public void remove(int index){
		getOptionValues().remove(index);
		getOptionNames().remove(index);
	}
	
	public void setSelectedValue(String value) {
		for(int i=0;  i < getOptionValues().size(); i++) {
			if(value.equals(getOptionValues().get(i))) {
				setSelected(value);
				setSelectedText(getOptionNames().get(i));
				
				break;
			}
		}
	}
	
	public void setSelectedTextValue(String value) {
		for(int i=0;  i < getOptionNames().size(); i++) {
			if(value.equals(getOptionNames().get(i))) {
				setSelectedText(value);
				setSelected(getOptionValues().get(i));
				
				break;
			}
		}
	}
	
	public int getSize(){
		return this.getOptionValues().size();
	}
	
	public void onLoad() throws Exception {
		
	}
}
