package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Range {	
	public String[] options() default {};
	public String[] values() default {};
	
	public int size() default -1;
}
