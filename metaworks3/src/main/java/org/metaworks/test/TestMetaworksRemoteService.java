package org.metaworks.test;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.context.ApplicationContext;

/**
 * Created by jangjinyoung on 15. 8. 6..
 */
public class TestMetaworksRemoteService extends MetaworksRemoteService{

    ApplicationContext applicationContext;
        public ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

    public TestMetaworksRemoteService(ApplicationContext applicationContext){
        setApplicationContext(applicationContext);

        setInstance(this);
    }


    @Override
    public ApplicationContext getBeanFactory() {
        return getApplicationContext();
    }
}
