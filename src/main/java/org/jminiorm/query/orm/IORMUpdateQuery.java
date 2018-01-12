package org.jminiorm.query.orm;

import org.jminiorm.exception.DBException;

import java.util.Collection;

/**
 * Represents an update query for objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface IORMUpdateQuery<T> extends IORMQuery<T> {

    IORMUpdateQuery<T> forClass(Class<T> clazz);

    /**
     * Add an object to those to be updated.
     *
     * @param obj
     * @return
     */
    IORMUpdateQuery<T> addOne(T obj);

    /**
     * Add a collection of objects to those to be updated.
     *
     * @param objs
     * @return
     */
    IORMUpdateQuery<T> addMany(Collection<T> objs);

    /**
     * Updates the objects collected so far.
     */
    void execute() throws DBException;

}
