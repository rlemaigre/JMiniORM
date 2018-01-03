package org.jminiorm.query;

import java.util.List;

/**
 * Represents a generic delete query, that is, one that deletes rows from an arbitrary table.
 */
public interface IDeleteQuery {

    /**
     * Sets the table.
     *
     * @param table
     * @return
     */
    IDeleteQuery table(String table);

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
