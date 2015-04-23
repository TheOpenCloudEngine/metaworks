package org.metaworks.query;

import java.util.*;

import org.metaworks.*;

public class And extends QuerySet{
	
	public And(Query [] q){
		super(q);
	}
	
	public String getSeparator(){
		return " and ";
	}
}