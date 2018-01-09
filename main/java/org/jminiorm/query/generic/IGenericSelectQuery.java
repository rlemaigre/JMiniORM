package org.jminiorm.query.generic;

import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;

import java.util.List;
import java.util.Map;

/**
 * Represents a generic select query, that is, one that may return objects of any type (not necessarily a JPA annotated
 * class). Since the return type may be anything, the from and select clauses cannot be infered from JPA annotations and
 * the whole SQL statement must be specified.
 */
public interface IGenericSelectQuery extends IGenericQuery {

    /**
     * Sets the whole select statement and returns this.
     *
     * @param sql
     * @param params
     * @return this
     */
    IGenericSelectQuery sql(String sql, Object... params);

    /**
     * Sets the limit and returns this.
     *
     * @param limit
     * @return
     */
    IGenericSelectQuery limit(Long limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    IGenericSelectQuery offset(Long offset);

    /**
     * Adds a column name => Java class mapping.
     *
     * @param column
     * @param javaClass
     */
    IGenericSelectQuery type(String column, Class<?> javaClass);

    /**
     * Specifies the column name => Java class mappings.
     *
     * @param typeMappings
     */
    IGenericSelectQuery types(Map<String, Class<?>> typeMappings);

    /**
     * Extracts the first result of the result set. Throws an exception if there is more than or less than one element
     * in the result set.
     *
     * @return
     * @throws UnexpectedNumberOfItemsException when there are more than one or zero items in the result set.
     * @throws DBException
     */
    <T> T one() throws UnexpectedNumberOfItemsException, DBException;

    /**
     * Extracts the first result of the result set.
     *
     * @return
     * @throws DBException
     */
    <T> T first() throws DBException;

    /**
     * Returns all the items in the result set.
     *
     * @return
     * @throws DBException
     */
    <T> List<T> list() throws DBException;

}
