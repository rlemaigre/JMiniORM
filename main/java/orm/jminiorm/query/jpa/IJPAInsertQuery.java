package orm.jminiorm.query.jpa;

import java.util.Collection;

/**
 * Represents an insert query for objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface IJPAInsertQuery<T> {

    /**
     * Add an object to those to be inserted.
     *
     * @param obj
     * @return
     */
    IJPAInsertQuery<T> add(T obj);

    /**
     * Add a collection of objects to those to be inserted.
     *
     * @param objs
     * @return
     */
    IJPAInsertQuery<T> add(Collection<T> objs);

    /**
     * Inserts the objects collected so far.
     */
    void execute();

}
