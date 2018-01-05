package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.IQuery;

public interface IRawQuery extends IQuery {

    /**
     * Sets the raw SQL string.
     *
     * @param sql
     */
    IRawQuery sql(String sql, Object... params);

    /**
     * Executes the query.
     *
     * @throws DBException
     */
    void execute() throws DBException;

}
