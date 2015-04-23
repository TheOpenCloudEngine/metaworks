package org.metaworks.test.faceclass;

import java.lang.reflect.Array;
import java.util.HashMap;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.test.MetaworksTest;
import org.metaworks.test.parameterizedcall.AutowiredObject;
import org.metaworks.test.parameterizedcall.Main;
import org.metaworks.test.parameterizedcall.Part;
import org.metaworks.test.parameterizedcall.PartSelectBox;
import org.metaworks.widget.Popup;

import junit.framework.TestCase;

public class FaceClassTest extends MetaworksTest{
	
	public void testFaceClassAutowire() throws Throwable{
		
//		HashMap wiringValues = new HashMap<String, Object>();
//		
//		Main main = new Main();
//		
//		wiringValues.put(ServiceMethodContext.WIRE_PARAM_CLS + AutowiredObject.class.getName(), main.getAutowiredObject());
//		
//		
//		Object returned = callMetaworksService(
//				Part.class.getName(),
//				main, 
//				"returnSecondPart", 
//				wiringValues 
//			);
//		
//		assertTrue("The real value should be changed to face class", returned instanceof PartSelectBox);
//		PartSelectBox partSelectBox = (PartSelectBox) returned;
//		
//		assertTrue("the face class (selectBox) should have options obtained from autowired value", partSelectBox.getOptionValues().size()==2);
//		assertTrue("the face class should have @AutowiredFromClient value", partSelectBox.autowiredObject!=null);
		
	}

}
