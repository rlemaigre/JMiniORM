package org.jminiorm.query;

import java.util.List;

/**
 * Represents a generic update query, that is, one that updates rows in an arbitrary table.
 */
public interface IUpdateQuery extends IQuery {

    /**
     * Sets the table and the primary key column.
     *
     * @param table
     * @param idColummn
     * @return
     */
    IUpdateQuery table(String table, String idColummn);

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
    void execute();

}
