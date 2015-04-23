package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tell this field is childs of the object. Normally the type of childs should be List of the object "List&lt;Object&gt;"
 * @author jyjang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Children {
}
