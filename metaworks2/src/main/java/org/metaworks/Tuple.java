package org.metaworks;

public interface Tuple{

		
	public TupleProperty getKeyProperty();	
	public TupleProperty[] getAllProperties();
	
	public void setProperty(TupleProperty prop);
	public void setProperties(TupleProperty[] props);		
	
	public void save() throws Exception;
	public void load(TupleProperty keyProperty) throws Exception;
	public void load(Object keyObject) throws Exception;	
}
