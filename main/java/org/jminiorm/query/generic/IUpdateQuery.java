package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.IQuery;

import java.util.List;
import java.util.Map;

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
     * Add one map of columns to update and their corresponding values. The map must contain the value for the idColumn
     * key.
     *
     * @param values
     * @return
     */
    IUpdateQuery addOne(Map<String, Object> values);

    /**
     * Add serveral maps of columns to update and their corresponding values. Each map must contain the value for the idColumn
     * key. All the map must have the same set of keys.
     *
     * @param values
     * @return
     */
    IUpdateQuery addMany(List<Map<String, Object>> values);

    /**
     * Executes the (batch) statement.
     */
    void execute() throws DBException;

}
