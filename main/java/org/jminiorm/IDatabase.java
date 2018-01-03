package org.jminiorm;

import orm.jminiorm.query.ITypedSelectQuery;

import java.sql.SQLException;

public interface IDatabase {

    /**
     * Executes an arbitrary SQL statement that returns nothing.
     *
     * @param sql
     * @param params
     * @throws SQLException
     */
    void sql(String sql, Object... params) throws SQLException;

    /**
     * Returns the instance of the JPA annotated class T of given id.
     *
     * @param clazz
     * @param id
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> T select(Class<T> clazz, Object id) throws SQLException;

    /**
     * Begins a select query on the instances of the JPA annotated class T.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> ITypedSelectQuery<T> select(Class<T> clazz) throws SQLException;

}
