package org.metaworks;

public abstract class Dependancy {
	
	public Dependancy(String dependancyFieldName){
		setDependantFieldName(dependancyFieldName);
	}
	
	String dependantFieldName;
		public String getDependantFieldName() {
			return dependantFieldName;
		}
		public void setDependantFieldName(String dependantFieldName) {
			this.dependantFieldName = dependantFieldName;
		}

	abstract public void action(FieldDescriptor myFieldDescriptor, FieldDescriptor theDependantFieldDescriptor);

}
