package org.jminiorm.query.jpa;

import java.util.Collection;

/**
 * Represents an update query for objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface IJPAUpdateQuery<T> {

    /**
     * Add an object to those to be updated.
     *
     * @param obj
     * @return
     */
    IJPAUpdateQuery<T> addOne(T obj);

    /**
     * Add a collection of objects to those to be updated.
     *
     * @param objs
     * @return
     */
    IJPAUpdateQuery<T> addMany(Collection<T> objs);

    /**
     * Updates the objects collected so far.
     */
    void execute();

}
