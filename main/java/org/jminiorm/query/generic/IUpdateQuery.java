package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.IQuery;

import java.util.List;

/**
 * Represents a generic update query, that is, one that updates rows in an arbitrary table.
 */
public interface IUpdateQuery extends IQuery {

    /**
     * Sets the table and the primary key column.
     *
     * @param table
     * @return
     */
    IUpdateQuery table(String table);

    /**
     * Sets the column containing the id.
     *
     * @param idColumn
     * @return
     */
    IUpdateQuery idColumn(String idColumn);

    /**
     * Sets the columns.
     *
     * @param cols
     * @return
     */
    IUpdateQuery columns(List<String> cols);

    /**
     * Add one set of values.
     *
     * @param values
     * @return
     */
    IUpdateQuery addOne(Object id, List<Object> values);

    /**
     * Add many "values(...) clauses".
     *
     * @param values
     * @return
     */
    IUpdateQuery addMany(List<Object> ids, List<List<Object>> values);

    /**
     * Executes the (batch) statement.
     */
    void execute() throws DBException;

}
