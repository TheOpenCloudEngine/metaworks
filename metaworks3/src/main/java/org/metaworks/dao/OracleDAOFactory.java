/*
 * Created on 2004. 11. 3.
 */
package org.metaworks.dao;

import java.util.Calendar;
import java.util.Map;



/**
 * @author Jinyoung Jang
 */
public class OracleDAOFactory extends DAOFactory{
	
	

	public String getDBMSProductName() throws Exception {
		return "Oracle";
	}

	

	@Override
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		//return " (select SEQ_BPM_"+ seqName + ".NextVal as keyNumber from dual) "; 
		return " (select "+ seqName + ".NextVal as keyNumber from dual) ";
	}



	@Override
	public KeyGeneratorDAO createKeyGenerator(String forWhat, Map options)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
