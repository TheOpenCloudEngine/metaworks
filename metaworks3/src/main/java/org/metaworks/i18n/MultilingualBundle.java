package org.metaworks.i18n;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjy on 2015. 12. 30..
 */
public class MultilingualBundle implements MultilingualSupport{

    Map<String, Map<String, String>> bundleData;
        public Map<String, Map<String, String>> getBundleData() {
            return bundleData;
        }

        public void setBundleData(Map<String, Map<String, String>> bundleData) {
            this.bundleData = bundleData;
        }


    @Override
    public void putMultilingualText(String language, String propertyName, String value) {
        if(bundleData==null){
            bundleData = new HashMap<String, Map<String, String>>();

        }

        Map<String, String> bundleForLang;
        if(bundleData.containsKey(language)){
            bundleForLang = bundleData.get(language);
        }else{
            bundleForLang = new HashMap<String, String>();
            bundleData.put(language, bundleForLang);
        }

        bundleForLang.put(propertyName, value);

    }

    @Override
    public String getMultilingualText(String language, String propertyName) {
        if(bundleData==null) return null;
        if(!bundleData.containsKey(language)) return null;
        Map<String, String> bundleForLang = bundleData.get(language);
        if(!bundleForLang.containsKey(propertyName)) return null;

        return bundleForLang.get(propertyName);
    }
}
