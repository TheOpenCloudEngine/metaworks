/*
 * Created on 2005. 1. 25.
 */
package org.metaworks.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jinyoung Jang
 */
public class UniqueKeyGenerator {

	public static Long issueKey(String forWhat, ConnectionFactory cf) throws Exception{
		return issueKey(forWhat, null, cf);
	}
	
	public static Long issueKey(String forWhat, Map options, ConnectionFactory cf) throws Exception{
		KeyGeneratorDAO kg = DAOFactory.getInstance(cf).createKeyGenerator(forWhat, options);
		kg.select();
		kg.next();
				
		Number number = kg.getKeyNumber();
		
		if(number!=null){
			return new Long(number.longValue());
		}else		
			return null;		
	}
	
	public static Long issueProcessDefinitionKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcDef", cf);
	}

	public static Long issueProcessDefinitionVersionKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcDefVer", cf);
	}
	
	public static Long issueProcessInstanceKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcInst", cf);
	}

	public static Long issueProcessVariableKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcVar", cf);
	}

	public static Long issueRoleMappingKey(ConnectionFactory cf) throws Exception{
		Map options = new HashMap();
		options.put("useTableNameHeader", true);

		return issueKey("RoleMapping", options, cf);
	}
	
	public static Long issueWorkItemKey(ConnectionFactory cf) throws Exception{
		return issueKey("WorkItem", cf);
	}
}
