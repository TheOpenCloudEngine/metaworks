package org.metaworks.test.faceclass;



import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.component.SelectBox;

@Face(faceClass=CompetencySelectBox.class)
public class Competency{

	public Competency(String string) {
		setName(string);
	}
	
	String name;

		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
}