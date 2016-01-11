package org.metaworks.component;

import java.io.Serializable;

import org.metaworks.common.ChoiceBox;
import org.metaworks.dao.IDAO;

public class SelectBox extends ChoiceBox implements Serializable{
	int selectSize;
		public int getSelectSize() {
			return selectSize;
		}
		public void setSelectSize(int selectSize) {
			this.selectSize = selectSize;
		}
	String selectStyle;
		public String getSelectStyle() {
			return selectStyle;
		}
		public void setSelectStyle(String selectStyle) {
			this.selectStyle = selectStyle;
		}

	boolean multiple;
		public boolean isMultiple() {
			return multiple;
		}
		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}


	public SelectBox(){
		super();
		setSelectSize(1);
		setSelectStyle("");
	}
	
	public SelectBox(IDAO dao){		
		super(dao);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getSelected().equals(obj);
	}
	
	public String toString(){
		return this.getSelected();
	}
}
