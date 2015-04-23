package org.metaworks.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAOUtil {

	boolean makeConnection;
		public boolean isMakeConnection() {
			return makeConnection;
		}
		public void setMakeConnection(boolean makeConnection) {
			this.makeConnection = makeConnection;
		}

	String databaseProductName;
		public String getDatabaseProductName(){
			if(this.databaseProductName == null){
				try {
					this.databaseProductName = DAOFactory.getInstance().getDBMSProductName();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			return this.databaseProductName;
		}
		public void setDatabaseProductName(String databaseProductName) {
			this.databaseProductName = databaseProductName;
		}
		
	List<String> reservedWord;
		public List<String> getReservedWord() {
			if(this.reservedWord == null){
				if("CUBRID".equals(this.getDatabaseProductName())) {
					this.reservedWord = Arrays.asList(new String[]{"NO"});
				}else{
					this.reservedWord = new ArrayList<String>();
				}
			}
			
			return this.reservedWord;
		}
		public void setReservedWord(List<String> reservedWord) {
			this.reservedWord = reservedWord;
		}		
		
	public DAOUtil(){
	}
	
	public String replaceReservedKeyword(String propertyName) {
		
		if(this.getReservedWord().indexOf(propertyName.toUpperCase()) > -1){
			if("CUBRID".equals(this.getDatabaseProductName())) {
				propertyName = "\"" + propertyName + "\"";
			}
		}
		
		return propertyName;
	}

}