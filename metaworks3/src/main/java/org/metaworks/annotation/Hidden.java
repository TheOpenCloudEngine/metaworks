package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Hidden {
	boolean on() default true;
	String[] when() default {};
	String[] where() default {};
	String[] how() default {};
	String[] media() default {};	
}
