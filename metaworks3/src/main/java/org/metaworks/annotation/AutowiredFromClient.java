package org.metaworks.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Autowire the client's object
 * example:
 * 
 * 
 * 
 * 
 * @author jyjang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface AutowiredFromClient {
	boolean onDrop() default false;
	String instruction() default "";
	String select() default "";
}
