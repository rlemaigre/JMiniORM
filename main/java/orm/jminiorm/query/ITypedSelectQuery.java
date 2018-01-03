package orm.jminiorm.query;

import org.jminiorm.resultset.IResultSet;

/**
 * Represents a typed select query, that is, one that returns objects of a JPA annotated class.
 *
 * @param <T>
 */
public interface ITypedSelectQuery<T> extends IQuery, IResultSet<T> {

    /**
     * Specifies the where clause and returns this.
     *
     * @param where
     * @param params
     * @return this
     */
    ITypedSelectQuery<T> where(String where, Object... params);

    /**
     * Specifies the order by clause and returns this.
     *
     * @param orderBy
     * @return this
     */
    ITypedSelectQuery<T> orderBy(String orderBy);

}
