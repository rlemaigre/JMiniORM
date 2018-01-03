package orm.jminiorm.query;

import org.jminiorm.resultset.IResultSet;

/**
 * Represents an untyped select query, that is, one that may return objects of any type (not necessarily a JPA annotated
 * class).
 */
public interface IUntypedSelectQuery extends IQuery {

    /**
     * Turns this query into a result set of objects of the given type (which may or may not be a JPA annotated class,
     * for example a HashMap or an Integer).
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> IResultSet<T> as(Class<T> clazz);

}
