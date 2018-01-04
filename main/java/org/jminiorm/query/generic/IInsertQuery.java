package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.IQuery;

import java.util.List;

/**
 * Represents a generic insert query, that is, one that insert rows in an arbitrary table.
 */
public interface IInsertQuery extends IQuery {

    /**
     * Sets the table.
     *
     * @param table
     * @return
     */
    IInsertQuery table(String table);

    /**
     * Sets the columns.
     *
     * @param cols
     * @return
     */
    IInsertQuery columns(List<String> cols);

    /**
     * Add one values(...) clause.
     *
     * @param values
     * @return
     */
    IInsertQuery addOne(List<Object> values);

    /**
     * Add many values(...) clauses.
     *
     * @param values
     * @return
     */
    IInsertQuery addMany(List<List<Object>> values);

    /**
     * Executes the (batch) statement and returns the generated ids.
     */
    List<Object> execute() throws DBException;

}
