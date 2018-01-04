package org.jminiorm.query.generic;

import org.jminiorm.query.IQuery;

import java.util.List;

/**
 * Represents a generic delete query, that is, one that deletes rows from an arbitrary table.
 */
public interface IDeleteQuery extends IQuery {

    /**
     * Sets the table.
     *
     * @param table
     * @param idColumn
     * @return
     */
    IDeleteQuery table(String table, String idColumn);

    /**
     * Add one id.
     *
     * @param id
     * @return
     */
    IDeleteQuery addOne(Object id);

    /**
     * Add many ids.
     *
     * @param ids
     * @return
     */
    IDeleteQuery addMany(List<Object> ids);

    /**
     * Executes the (batch) statement.
     */
    void execute();

}
