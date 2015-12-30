package org.metaworks.i18n;

/**
 * Created by jjy on 2015. 12. 30..
 */
public interface MultilingualSupport {
    public void putMultilingualText(String language, String propertyName, String value);
    public String getMultilingualText(String language, String propertyName);
}
