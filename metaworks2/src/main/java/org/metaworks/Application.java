package org.metaworks;

public interface Application{

	public void newInstance();		

	public Instance[] getInstances();
	public void addInstance(Instance rec);
	public void addInstances(Instance [] recs);
	
	public void setType(Type table);
	public Type getType();
	public javax.swing.JPanel createPanel();
}