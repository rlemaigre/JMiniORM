package org.jminiorm.query.orm;

import org.jminiorm.resultset.IObjectResultSet;

/**
 * Represents a select query that returns objects of a JPA annotated class. The select and from clauses are infered from
 * the JPA annotations.
 *
 * @param <T>
 */
public interface IORMSelectQuery<T> extends IORMQuery<T>, IObjectResultSet<T> {

    @Override
    IORMSelectQuery<T> forClass(Class<T> clazz);

    /**
     * Sets the where clause and returns this.
     *
     * @param where
     * @param params
     * @return this
     */
    IORMSelectQuery<T> where(String where, Object... params);

    /**
     * Sets the id of the object to return.
     *
     * @param id
     * @return this
     */
    IORMSelectQuery<T> id(Object id);

    /**
     * Sets the limit and returns this.
     *
     * @param limit
     * @return
     */
    IORMSelectQuery<T> limit(Integer limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    IORMSelectQuery<T> offset(Integer offset);

    /**
     * Sets the order by clause and returns this. Optional.
     *
     * @param orderBy
     * @return this
     */
    IORMSelectQuery<T> orderBy(String orderBy);

}
