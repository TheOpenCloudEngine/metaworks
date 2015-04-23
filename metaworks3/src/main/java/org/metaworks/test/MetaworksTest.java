package org.metaworks.test;

import java.util.Map;

import junit.framework.TestCase;

import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksConverter;
import org.metaworks.dwr.MetaworksRemoteService;

public class MetaworksTest extends TestCase{
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		
		new TransactionContext();
		
		
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		
		TransactionContext.getThreadLocalInstance().commit();
	}
	
	protected Object callMetaworksService(String className, Object clientObject, String methodName, Map<String, Object> wiringValues) throws Throwable{
		

		Object returned = MetaworksRemoteService.getInstance().callMetaworksService(
				className,
				clientObject, 
				methodName, 
				wiringValues 
			);
		
		//returnMetaworksConverter.convertFaceClassOutbound(returned);
		
		return returned;		
	}
	
}
