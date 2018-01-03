package orm.jminiorm.query;

import org.jminiorm.resultset.IResultSet;

/**
 * Represents a generic select query, that is, one that may return objects of any type (not necessarily a JPA annotated
 * class). Since the return type may be anything, the from and select clauses cannot be infered from JPA annotations and
 * the whole SQL statement must be specified.
 */
public interface ISelectQuery extends IQuery {

    /**
     * Sets the whole select statement and returns this.
     *
     * @param sql
     * @param params
     * @return this
     */
    ISelectQuery sql(String sql, Object... params);

    /**
     * Sets the limit and returns this.
     *
     * @param limit
     * @return
     */
    ISelectQuery limit(long limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    ISelectQuery offset(long offset);

    /**
     * Turns this query into a result set of objects of the given type (which may or may not be a JPA annotated class,
     * for example a HashMap or an Integer). If the given type is a JPA annotated class, then JPA annotation are used to
     * convert from columns aliases to Java properties.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> IResultSet<T> as(Class<T> clazz);

}
