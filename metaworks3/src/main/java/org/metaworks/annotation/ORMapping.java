package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ORMapping {
	String[] objectFields();
	String[] databaseFields();
	boolean objectIsNullWhenFirstDBFieldIsNull() default false;
	String[] availableWhen() default "";
}
