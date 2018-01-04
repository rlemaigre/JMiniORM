package org.jminiorm.query.orm;

import org.jminiorm.query.IQuery;

public interface IORMQuery<T> extends IQuery {

    /**
     * The JPA annotated class this query is about.
     *
     * @param clazz
     */
    void forClass(Class<T> clazz);

}
