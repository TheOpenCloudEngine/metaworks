package org.metaworks.dao;

public class TypeSelectorException extends Exception {
	public TypeSelectorException(){
		super("There's no type selector in your IDAO declaration. Use 'TypeSelector' annotation to use 'cast()'.");
	}
}
