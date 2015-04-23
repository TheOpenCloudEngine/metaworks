package org.metaworks.inputter;

import java.awt.Component;
import java.util.Hashtable;
import java.sql.*;
import org.metaworks.*;

public abstract class FullReferenceInput extends AbstractComponentInputter{

	Connection con;
	String sqlStmt;
	String refTableName, carryingFieldName, dispFieldName;
	
	Instance selectedRec=null;

	public FullReferenceInput(){
		super();
		// do nothing
	}

	public FullReferenceInput( Connection con, String refTableName, String carryingFieldName, String dispFieldName){
		super();
		
		this.con = con;	
		this.refTableName = refTableName;
		this.carryingFieldName = refTableName;
		this.dispFieldName = dispFieldName;
	}
	
////////////////// Overrides //////////////////////

	public void initialize(Hashtable attrs){
		super.initialize(attrs);
		
		uiComp=getNewComponent();
		
		if(attrs.containsKey(FieldDescriptor.ATTR_DEFAULT))
			setValue(attrs.get(FieldDescriptor.ATTR_DEFAULT));
			
		if(attrs.containsKey(FieldDescriptor.ATTR_FIXED)){
			setValue(attrs.get(FieldDescriptor.ATTR_FIXED));
			getComponent().setEnabled(false);
		}
		
		
		if(attrs.containsKey("mandatory")){
			bMandatory=true;
		}
	}
	
/////////// implemetations //////////////

	public Object getValue(){
		return null;
	}
	
	public void setValue(Object data){
		
	}
	
	public Component getNewComponent(){
		return null;
	}
			
}
