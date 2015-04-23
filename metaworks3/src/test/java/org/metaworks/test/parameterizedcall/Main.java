package org.metaworks.test.parameterizedcall;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;

public class Main {
	
	public final static String idValue = "ID";
	public final static String valueValue = "VALUE";

	
	public Main(){

		setPart(new Part());
		
		setAutowiredObject(new AutowiredObject());
		getAutowiredObject().setValue(valueValue);
		getAutowiredObject().setId(idValue);
	}
	
	Part part;
		public Part getPart() {
			return part;
		}
	
		public void setPart(Part part) {
			this.part = part;
		}

	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public SecondPart returnSecondPart(){
		return new SecondPart();
	}	
		
	AutowiredObject autowiredObject;
	@AutowiredToClient
	@Hidden
		public AutowiredObject getAutowiredObject() {
			return autowiredObject;
		}
	
		public void setAutowiredObject(AutowiredObject autowiredObject) {
			this.autowiredObject = autowiredObject;
		}

}
