package org.metaworks.test.parameterizedcall;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Popup;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class Part{
	

	@ServiceMethod
	public static Part create( String test, @Payload("id") String id, @Payload("value") String value, @AutowiredFromClient AutowiredObject autowired){
		Part parameterizedCallTest = new Part();
		
		parameterizedCallTest.setId(autowired.getId() + "&" + id);
		parameterizedCallTest.setValue(autowired.getValue() + "&" + value);
		
		
		return parameterizedCallTest;
	}

	
	@ServiceMethod
	public void action(){
		setValue("value2");
	
	}
	
	
//	@ServiceMethod
//	public Pair<AutowiredObject, Remover> typedReturn(){
//		return Pair.with(new AutowiredObject(), new Remover(this));
//	}
//	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Part returnWithUIElements(){
		
		Part part = new Part();
		
		if(metaworksCall())
			wrapReturn(
				new Remover(new AutowiredObject()), 
				new Popup(part)
			);
		
		return part;
	}
	

	
	@ServiceMethod
	public void returnMySelf(){
		
		setValue("Value changed by myself");

	}
	
	
	
	String id;
	
		public String getId() {
			return id;
		}
	
		public void setId(String id) {
			this.id = id;
		}

	String value;
		public String getValue() {
			return value;
		}
	
		public void setValue(String value) {
			this.value = value;
		}

		
		

	
	
	
	
}
