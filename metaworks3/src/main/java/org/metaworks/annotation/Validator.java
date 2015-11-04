package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
	public final static String VALIDATE_NOTNULL = "notnull";
	public final static String VALIDATE_NULL = "null";
	public final static String VALIDATE_MAXBYTE = "maxbyte";
	public final static String VALIDATE_MAX = "maxlength";
	public final static String VALIDATE_MIN = "minlength";
	public final static String VALIDATE_NUMBERZERO = "numberzero";
	public final static String VALIDATE_REGULAREXPRESSION = "regularexpression";
	public final static String VALIDATE_CONDITION = "condition";
	public final static String VALIDATE_ASSERTTRUE = "asserttrue";
	public final static String VALIDATE_ASSERTFALSE = "assertfalse";


	String name() default "";
	String message() default "";
	String condition() default "";	
	String availableUnder() default "";
	
	
	String[] options() default {};
}