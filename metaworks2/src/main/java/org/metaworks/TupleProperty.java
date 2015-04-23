package org.metaworks;

public interface TupleProperty{
	
	public AbstractPropertyDescriptor getPropertyDescriptor();
	public Object getValueObject();
	public int getType();
	public String getName();
}
		