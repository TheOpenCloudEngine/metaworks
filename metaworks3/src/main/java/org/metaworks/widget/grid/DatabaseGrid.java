package org.metaworks.widget.grid;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;


public class DatabaseGrid extends ObjectGrid{
	
	
	public DatabaseGrid() throws Exception {
		super();
	}
	
	

	public void setObjectData(Class dataClass, IDAO dao) throws Exception {
		
		setObjectType(dataClass);
		setData(dao);
	}
	
	@Override
	@ServiceMethod(callByContent = true, target="none")
	public void changeCell() {
		
		try {
			Class daoClass = Thread.currentThread().getContextClassLoader().loadClass(getClassName());
						
			IDAO dao = Database.get(daoClass, getCell().getRowId());
			
			dao.set(getCell().getName(), getCell().getValue());
			
			throw new Exception("error");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
