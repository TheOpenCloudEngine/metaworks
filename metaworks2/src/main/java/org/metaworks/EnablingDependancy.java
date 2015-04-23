package org.metaworks;

public abstract class EnablingDependancy extends Dependancy{
	
	public EnablingDependancy(String dependFd){
		super(dependFd);
	}

	public void action(FieldDescriptor myFieldDescriptor, FieldDescriptor theDependantFieldDescriptor) {
		Object theValue = theDependantFieldDescriptor.getInputter().getValue();
		myFieldDescriptor.getInputComponent().setEnabled(enableIf(theValue));			
	}
	
	abstract public boolean enableIf(Object dependencyFieldValue);
		
}
