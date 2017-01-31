package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark this field is not intended to be uploaded to the server. All the object paths should be pruned recusively when it is uploaded."
 * @author jyjang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DontUpload {
}
