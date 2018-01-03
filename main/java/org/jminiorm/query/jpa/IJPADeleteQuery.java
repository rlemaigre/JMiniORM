package org.jminiorm.query.jpa;

import org.jminiorm.query.IQuery;

import java.util.Collection;

/**
 * Represents an delete query for objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface IJPADeleteQuery<T> extends IQuery {

    /**
     * Add an object to those to be deleted.
     *
     * @param obj
     * @return
     */
    IJPADeleteQuery<T> addOne(T obj);

    /**
     * Add a collection of objects to those to be deleted.
     *
     * @param objs
     * @return
     */
    IJPADeleteQuery<T> addMany(Collection<T> objs);

    /**
     * Deletes the objects collected so far.
     */
    void execute();

}
