package org.jminiorm.query.generic;

import org.jminiorm.resultset.IMapResultSet;
import org.jminiorm.resultset.IObjectResultSet;
import org.jminiorm.resultset.IPrimitiveResultSet;

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
    IGenericSelectQuery limit(Integer limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    IGenericSelectQuery offset(Integer offset);

    /**
     * Returns the result set as objects of primitive type T.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> IPrimitiveResultSet<T> asPrimitive(Class<T> clazz);

    /**
     * Returns the result set as Maps String Object.
     *
     * @return
     */
    IMapResultSet<Object> asMap();

    /**
     * Returns the result set as Maps String T.
     *
     * @return
     */
    <T> IMapResultSet<T> asMap(Class<T> type);

    /**
     * Returns the result set as objects of type T.
     *
     * @return
     */
    <T> IObjectResultSet<T> asObject(Class<T> clazz);

}
