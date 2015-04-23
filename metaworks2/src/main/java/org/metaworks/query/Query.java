package org.metaworks.query;

import org.metaworks.*;

public interface Query{
		
	public String toSQLExp(Instance rec);
	
	public String [] getFieldNames();

}
