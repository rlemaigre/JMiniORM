package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;

import java.util.List;

/**
 * Represents a generic delete query, that is, one that deletes rows from an arbitrary table.
 */
public interface IGenericDeleteQuery extends IGenericQuery {

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
     * Add one id.
     *
     * @param id
     * @return
     */
    IGenericDeleteQuery addOne(Object id);

    /**
     * Add many ids.
     *
     * @param ids
     * @return
     */
    IGenericDeleteQuery addMany(List<Object> ids);

    /**
     * Executes the (batch) statement.
     */
    void execute() throws DBException;

}
