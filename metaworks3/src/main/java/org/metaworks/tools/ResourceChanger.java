package org.metaworks.tools;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by jjy on 2016. 10. 18..
 */
public class ResourceChanger {

    static HashMap<String, String> changedResources = new HashMap<String, String>();

    String target;
        public String getTarget() {
            return target;
        }
        public void setTarget(String target) {
            this.target = target;
        }

    String sourceCode;
    @Face(ejsPath="genericfaces/richText.ejs")
        public String getSourceCode() {
            return sourceCode;
        }
        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }


    @ServiceMethod(callByContent = true)
    public void change() throws Exception {
//        if(MetaworksRemoteService.getInstance().isDebugMode()){
            changedResources.put(getTarget(), getSourceCode());
//        }else{
//            throw new Exception("This is not debug mode.");
//        }
    }


    public static InputStream getInputStream(String pathInfo) {
        if(changedResources==null || !changedResources.containsKey(pathInfo))
            return null;

        ByteArrayInputStream bai = new ByteArrayInputStream(changedResources.get(pathInfo).getBytes());

        return bai;
    }
}
