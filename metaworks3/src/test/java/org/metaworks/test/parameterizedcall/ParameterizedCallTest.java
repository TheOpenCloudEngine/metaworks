package org.metaworks.test.parameterizedcall;

import java.util.HashMap;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.test.MetaworksTest;

public class ParameterizedCallTest extends MetaworksTest{
	


	public void testParameterizedCall() throws Throwable{
		
		HashMap wiringValues = new HashMap<String, Object>();
		
		Main main = new Main();
		
		wiringValues.put(ServiceMethodContext.WIRE_PARAM_CLS + AutowiredObject.class.getName(), main.getAutowiredObject());
		
		
		Object returned = callMetaworksService(
				Part.class.getName(),
				main.getPart(), 
				"create", 
				wiringValues 
			);
		
		assertTrue(returned instanceof Part);
		
		Part me = (Part)returned;
		
		assertNotNull(me);
		assertEquals("Returned id must be composite value by autowired and parameters:", Main.idValue + "&null", me.getId());
		assertEquals(Main.valueValue + "&null", me.getValue());
		
	}

	public void testNoArgumentCall() throws Throwable{
		
		HashMap wiringValues = new HashMap<String, Object>();
		
		Main main = new Main();
		
		Object returned = callMetaworksService(
				Part.class.getName(),
				main.getPart(), 
				"action", 
				wiringValues 
			);
		
		assertTrue(returned instanceof Part);
		
		Part me = (Part)returned;
		
		assertNotNull(me);
		assertNull(me.getId());
		assertEquals("value2", me.getValue());
		
	}

//	public void testTypedReturnCall() throws Throwable{
//		
//		HashMap wiringValues = new HashMap<String, Object>();
//		
//		Main main = new Main();
//		
//		Object returned = callMetaworksService(
//				Part.class.getName(),
//				main.getPart(), 
//				"typedReturn", 
//				wiringValues 
//			);
//		
//		assertTrue("Returned value must be Object[]", returned instanceof Object[]);
//		
//		Object[] returnedArr = (Object[])returned;
//		assertTrue("First object must be an AutowiredObject", returnedArr[0] instanceof AutowiredObject);
//		assertTrue("Second object must be an Remover", returnedArr[1] instanceof Remover);
//		
//	}
	
//	public void testReturnWithUIElements() throws Throwable{
//		
//		HashMap wiringValues = new HashMap<String, Object>();
//		
//		Main main = new Main();
//		
//		Object returned = callMetaworksService(
//				Part.class.getName(),
//				main.getPart(), 
//				"returnWithUIElements", 
//				wiringValues 
//			);
//		
//		assertTrue("Returned value must be Object[]", returned instanceof Object[]);
//		
//		Object[] returnedArr = (Object[])returned;
//		assertTrue("First object must be an AutowiredObject", returnedArr[0] instanceof Remover);
//		assertTrue("Second object must be an Remover", returnedArr[1] instanceof Part);
//		
//	}
		
	public void testValueChangedByMySelf() throws Throwable{
		
		HashMap wiringValues = new HashMap<String, Object>();
		
		Main main = new Main();
		
		Object returned = callMetaworksService(
				Part.class.getName(),
				main.getPart(), 
				"returnMySelf", 
				wiringValues 
			);
		
		assertTrue("Changed Value must be a Part", returned instanceof Part);
		assertEquals("Changed Value must be 'Value changed by myself'", "Value changed by myself", ((Part)returned).getValue());
		
	}
		
	
	
	
	
}
