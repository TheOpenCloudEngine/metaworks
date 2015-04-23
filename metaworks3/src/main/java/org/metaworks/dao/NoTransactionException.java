package org.metaworks.dao;

public class NoTransactionException extends Exception {
    public NoTransactionException(String s) {
        super(s);
    }
}
