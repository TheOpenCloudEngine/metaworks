package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NonEditable {
	String[] when() default {};
	String[] where() default {};
	String[] how() default {};
}
