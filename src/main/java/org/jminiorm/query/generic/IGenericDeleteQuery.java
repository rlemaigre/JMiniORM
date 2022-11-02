package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;

import java.util.List;

/**
 * Represents a generic delete query, that is, one that deletes rows from an arbitrary table.
 */
public interface IGenericDeleteQuery extends IGenericQuery {

    /**
     * Sets the schema.
     *
     * @param schema
     * @return
     */
    IGenericDeleteQuery schema(String schema);

    /**
     * Sets the table.
     *
     * @param table
     * @return
     */
    IGenericDeleteQuery table(String table);

    /**
     * Sets the column containing the id.
     *
     * @param idColumn
     * @return
     */
    IGenericDeleteQuery idColumn(String idColumn);

    /**
     * Sets the columns containing the ids.
     */
    IGenericDeleteQuery idColumns(String... idColumns);

    /**
     * Add one id.
     *
     * @param id
     * @return
     */
    IGenericDeleteQuery addOne(Object... id);

    /**
     * Add many ids.
     *
     * @param ids
     * @return
     */
    IGenericDeleteQuery addMany(List<Object> ids);

    /**
     * Sets the where clause and returns this.
     *
     * @param sql
     * @param params
     * @return this
     */
    IGenericDeleteQuery where(String sql, Object... params);

    /**
     * Executes the (batch) statement.
     */
    void execute() throws DBException;

}
