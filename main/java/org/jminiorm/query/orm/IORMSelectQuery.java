package org.jminiorm.query.orm;

import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;

import java.util.List;
import java.util.Map;

/**
 * Represents a select query that returns objects of a JPA annotated class. The select and from clauses are infered from
 * the JPA annotations.
 *
 * @param <T>
 */
public interface IORMSelectQuery<T> extends IORMQuery<T> {

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

    /**
     * Extracts the first result of the result set. Throws an exception if there is more than or less than one element
     * in the result set.
     *
     * @return
     * @throws UnexpectedNumberOfItemsException when there are more than one or zero items in the result set.
     * @throws DBException
     */
    T one() throws UnexpectedNumberOfItemsException, DBException;

    /**
     * Extracts the first result of the result set.
     *
     * @return
     * @throws DBException
     */
    T first() throws DBException;

    /**
     * Returns all the items in the result set.
     *
     * @return
     * @throws DBException
     */
    List<T> list() throws DBException;

    /**
     * Returns all the items in the result set indexed by the given property.
     *
     * @return
     * @throws DBException
     */
    <K> Map<K, List<T>> index(String property) throws DBException;

    /**
     * Returns all the items in the result set indexed by the given property. Throws exception if the same value for the
     * given column is found more than once.
     *
     * @return
     * @throws DBException
     */
    <K> Map<K, T> uniqueIndex(String property) throws DBException;

}
