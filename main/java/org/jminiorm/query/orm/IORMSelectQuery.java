package org.jminiorm.query.orm;

import org.jminiorm.query.IQuery;
import org.jminiorm.resultset.IResultSet;

/**
 * Represents a select query that returns objects of a JPA annotated class. The select and from clauses are infered from
 * the JPA annotations.
 *
 * @param <T>
 */
public interface IORMSelectQuery<T> extends IQuery, IResultSet<T> {

    /**
     * Sets the JPA annotated class to select from and returns this. Required.
     *
     * @param clazz
     * @return this
     */
    IORMSelectQuery<T> from(Class<T> clazz);

    /**
     * Sets the where clause and returns this. Optional.
     *
     * @param where
     * @param params
     * @return this
     */
    IORMSelectQuery<T> where(String where, Object... params);

    /**
     * Sets the limit and returns this.
     *
     * @param limit
     * @return
     */
    IORMSelectQuery<T> limit(Long limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    IORMSelectQuery<T> offset(Long offset);

    /**
     * Sets the order by clause and returns this. Optional.
     *
     * @param orderBy
     * @return this
     */
    IORMSelectQuery<T> orderBy(String orderBy);

}
