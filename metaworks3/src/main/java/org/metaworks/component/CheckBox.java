package org.metaworks.component;

import java.io.Serializable;

import org.metaworks.common.ChoiceBox;

public class CheckBox extends ChoiceBox implements Serializable {
	public CheckBox(){
		super();
	}
	boolean vertical;	
		/**
		 * true : 세로 , false : 가로 
		 */
		public boolean isVertical() {
			return vertical;
		}
		public void setVertical(boolean vertical) {
			this.vertical = vertical;
		}
		
	public String toString(){
		return this.getSelected();
	}
}
