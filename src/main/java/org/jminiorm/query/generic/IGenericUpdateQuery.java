package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;

import java.util.List;
import java.util.Map;

/**
 * Represents a generic update query, that is, one that updates rows in an arbitrary table.
 */
public interface IGenericUpdateQuery extends IGenericQuery {

    /**
     * Sets the schema.
     *
     * @param schema
     * @return
     */
    IGenericUpdateQuery schema(String schema);

    /**
     * Sets the table and the primary key column.
     *
     * @param table
     * @return
     */
    IGenericUpdateQuery table(String table);

    /**
     * Sets the column containing the id.
     *
     * @param idColumn
     * @return
     */
    IGenericUpdateQuery idColumn(String idColumn);

    /**
     * Sets the columns containing the id.
     */
    IGenericUpdateQuery idColumns(String... idColumns);

    /**
     * Add one map of columns to update and their corresponding values. The map must contain the value for the idColumn
     * key.
     *
     * @param values
     * @return
     */
    IGenericUpdateQuery addOne(Map<String, Object> values);

    /**
     * Add serveral maps of columns to update and their corresponding values. Each map must contain the value for the idColumn
     * key. All the map must have the same set of keys.
     *
     * @param values
     * @return
     */
    IGenericUpdateQuery addMany(List<Map<String, Object>> values);

    /**
     * Executes the (batch) statement.
     */
    void execute() throws DBException;

}
