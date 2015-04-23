package org.metaworks.dao;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.metaworks.spring.SpringConnectionFactory;

@Aspect
public class TransactionAdvice {

    SpringConnectionFactory connectionFactory;

        public SpringConnectionFactory getConnectionFactory() {
            return connectionFactory;
        }

        public void setConnectionFactory(SpringConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
        }

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void initiateTransaction(){

        if(TransactionContext.getThreadLocalInstance() != null)
            return;

        //CodiClassLoader clForSession = CodiClassLoader.createClassLoader(appName, tenantId);

        //Thread.currentThread().setContextClassLoader(clForSession);

        TransactionContext tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing.
        tx.setManagedTransaction(false);
        tx.setAutoCloseConnection(true);

        if(connectionFactory!=null)
            tx.setConnectionFactory(connectionFactory);

    }


/**
 * Since Spring TX Manager automatically manages the connections, we don't have to care about committing and rolling back.
 */


    @AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void commitTransaction() throws Exception {

        if(TransactionContext.getThreadLocalInstance()!=null)
            TransactionContext.getThreadLocalInstance().beforeCommit();  //we only need to call beforeCommit which flushes the changes
    }

    @AfterThrowing("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void rollbackTransaction() throws Exception {

        if(TransactionContext.getThreadLocalInstance()!=null)
            try{
                TransactionContext.getThreadLocalInstance().beforeRollback();
            }catch(Exception e){

            }
    }

//    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public void releaseTransaction() throws Exception {
//
//        if(TransactionContext.getThreadLocalInstance()!=null)
//            TransactionContext.getThreadLocalInstance().releaseResources();
//    }



}
