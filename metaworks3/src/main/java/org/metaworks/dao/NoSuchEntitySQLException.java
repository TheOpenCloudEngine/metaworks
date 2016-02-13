package org.metaworks.dao;

import java.sql.SQLException;

/**
 * Created by jjy on 2016. 2. 12..
 */
public class NoSuchEntitySQLException extends SQLException {
    public NoSuchEntitySQLException(String s) {
        super(s);
    }
}
