package org.jminiorm;

import org.jminiorm.exception.DBException;

/**
 * Represents a database transaction. The interface is the same as IDatabase + commit, rollback, close.
 */
public interface ITransaction extends IDatabase {

    void commit() throws DBException;

    void rollback() throws DBException;

    void close() throws DBException;

}
