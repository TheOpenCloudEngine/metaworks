package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.metaworks.dao.IDAO;

@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
	
	String def() 	default "";
	
}
