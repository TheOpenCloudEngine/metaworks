package org.metaworks.test.faceclass;

import java.util.List;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;

public class Activity {

		
	private Competency competency;
	@Order(1)	
		public Competency getCompetency() {
			return competency;
		}
		public void setCompetency(Competency competency) {
			this.competency = competency;
		}

	private Competency2 competency2;
	@Face(faceClass=Competency2SelectBox.class)
		public Competency2 getCompetency2() {
			return competency2;
		}
		public void setCompetency2(Competency2 competency2) {
			this.competency2 = competency2;
		}
		
	@ServiceMethod(callByContent=true)
	public void testValue(){
		System.out.println(getCompetency().getName());
		System.out.println(getCompetency2().getName());
	}
		
	public Activity(){
		setCompetency(new Competency("2"));
		setCompetency2(new Competency2("1"));
	}
}