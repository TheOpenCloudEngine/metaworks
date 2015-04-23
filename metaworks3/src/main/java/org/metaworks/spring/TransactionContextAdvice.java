package org.metaworks.spring;

import java.rmi.RemoteException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class TransactionContextAdvice {
    
    @Before("execution(* org.springframework.transaction.PlatformTransactionManager.commit(..))")
    public void beforeCommit() throws java.rmi.RemoteException {
    	try {
			TransactionContext.getThreadLocalInstance().commit();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
    }

    @Before("execution(* org.springframework.transaction.PlatformTransactionManager.rollback(..))")
    public void beforeRollback() throws java.rmi.RemoteException {
    	try {
	    	TransactionContext.getThreadLocalInstance().rollback();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
   }
}
