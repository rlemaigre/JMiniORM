package org.jminiorm;

import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.provider.IORMappingProvider;

public interface IDatabase extends IQueryTarget {

    /**
     * Creates a new transaction on the current database.
     *
     * @return
     * @throws DBException
     */
    ITransaction createTransaction() throws DBException;

    /**
     * Returns the SQL dialect in use for this database.
     *
     * @return
     */
    ISQLDialect getDialect();

    /**
     * Returns the object relational mapping provider in use with this database.
     *
     * @return
     */
    IORMappingProvider getORMappingProvider();

}
