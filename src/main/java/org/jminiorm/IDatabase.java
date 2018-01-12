package org.jminiorm;

import org.jminiorm.exception.DBException;

public interface IDatabase extends IQueryTarget {

    /**
     * Creates a new transaction on the current database.
     *
     * @return
     * @throws DBException
     */
    ITransaction createTransaction() throws DBException;

}
