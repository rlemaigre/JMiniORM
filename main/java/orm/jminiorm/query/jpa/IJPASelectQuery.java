package orm.jminiorm.query.jpa;

import org.jminiorm.resultset.IResultSet;
import orm.jminiorm.query.IQuery;

/**
 * Represents a select query that returns objects of a JPA annotated class. The select and from clauses are infered from
 * the JPA annotations.
 *
 * @param <T>
 */
public interface IJPASelectQuery<T> extends IQuery, IResultSet<T> {

    /**
     * Sets the JPA annotated class to select from and returns this. Required.
     *
     * @param clazz
     * @return this
     */
    IJPASelectQuery<T> from(Class<T> clazz);

    /**
     * Sets the where clause and returns this. Optional.
     *
     * @param where
     * @param params
     * @return this
     */
    IJPASelectQuery<T> where(String where, Object... params);

    /**
     * Sets the limit and returns this.
     *
     * @param limit
     * @return
     */
    IJPASelectQuery<T> limit(long limit);

    /**
     * Sets the offset and returns this.
     *
     * @param offset
     * @return
     */
    IJPASelectQuery<T> offset(long offset);

    /**
     * Sets the order by clause and returns this. Optional.
     *
     * @param orderBy
     * @return this
     */
    IJPASelectQuery<T> orderBy(String orderBy);

}
