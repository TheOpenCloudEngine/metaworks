package org.metaworks.test.returnwrapper;

import java.lang.reflect.Array;
import java.util.HashMap;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.test.MetaworksTest;
import org.metaworks.test.parameterizedcall.AutowiredObject;
import org.metaworks.test.parameterizedcall.Main;
import org.metaworks.test.parameterizedcall.Part;
import org.metaworks.widget.Popup;

import junit.framework.TestCase;

public class ReturnWrapperTest extends MetaworksTest{
	
	public void testWrapReturn() throws Throwable{
		
		HashMap wiringValues = new HashMap<String, Object>();
		
		Main main = new Main();
		
		wiringValues.put(ServiceMethodContext.WIRE_PARAM_CLS + AutowiredObject.class.getName(), main.getAutowiredObject());
		
		
		Object returned = callMetaworksService(
				Part.class.getName(),
				main.getPart(), 
				"returnWithUIElements", 
				wiringValues 
			);
		
		assertTrue(returned instanceof Object[]);
		
		Object[] returnValues = (Object[])returned;
		
		assertTrue("the returned elements should be 3", returnValues.length==2);
		assertTrue("the first element must be a Remover", returnValues[0] instanceof Remover);
		assertTrue("the second element must be a Popup", returnValues[1] instanceof Popup);
		assertTrue("the wrapper must be wrapping the part", ((Popup)returnValues[1]).getPanel() instanceof Part);
				
		
	}

}
