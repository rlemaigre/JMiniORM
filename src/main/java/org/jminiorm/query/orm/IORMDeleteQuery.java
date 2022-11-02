package org.jminiorm.query.orm;

import org.jminiorm.exception.DBException;

import java.util.Collection;

/**
 * Represents an delete query for objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface IORMDeleteQuery<T> extends IORMQuery<T> {

    @Override
    IORMDeleteQuery<T> forClass(Class<T> clazz);

    /**
     * Add an object id to those to be deleted.
     *
     * @param id
     * @return
     */
    IORMDeleteQuery<T> id(Object... id);

    /**
     * Add an object to those to be deleted.
     *
     * @param obj
     * @return
     */
    IORMDeleteQuery<T> addOne(T obj);

    /**
     * Add a collection of objects to those to be deleted.
     *
     * @param objs
     * @return
     */
    IORMDeleteQuery<T> addMany(Collection<T> objs);

    /**
     * Sets the where clause and returns this.
     *
     * @param where
     * @param params
     * @return this
     */
    IORMDeleteQuery<T> where(String where, Object... params);

    /**
     * Deletes the objects collected so far.
     */
    void execute() throws DBException;

}
