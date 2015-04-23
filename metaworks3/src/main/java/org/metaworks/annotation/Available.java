package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Available {
	String[] when() default {};
	String[] where() default {};
	String[] how() default {};
	String[] media() default {};
	String[] condition() default {};
}
