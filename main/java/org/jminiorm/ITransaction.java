package org.jminiorm;

import org.jminiorm.exception.DBException;

import java.io.Closeable;

/**
 * Represents a database transaction. The interface is the same as IDatabase + commit, rollback, close. Transactions
 * should be closed after use to reset and return the Connection to the pool.
 */
public interface ITransaction extends IQueryTarget, Closeable {

    void commit() throws DBException;

    void rollback() throws DBException;

}