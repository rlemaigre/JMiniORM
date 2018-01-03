package org.jminiorm;

import java.sql.SQLException;

/**
 * Represents a database transaction. The interface is the same as IDatabase + commit, rollback, close.
 */
public interface ITransaction extends IDatabase {

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
