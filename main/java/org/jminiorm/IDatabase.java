package org.jminiorm;

import org.jminiorm.query.IInsertQuery;
import org.jminiorm.query.ISelectQuery;
import org.jminiorm.query.jpa.IJPASelectQuery;

import java.sql.SQLException;
import java.util.Collection;

public interface IDatabase {

    /**
     * Executes an arbitrary SQL statement that returns nothing (UPDATE, DELETE, CREATE TABLE, etc.).
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
     * Begins a JPA select query.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> IJPASelectQuery<T> select(Class<T> clazz);

    /**
     * Begins a generic select query.
     *
     * @param sql    The whole SQL, except limit and offset.
     * @param params
     * @return
     */
    ISelectQuery select(String sql, Object... params);

    /**
     * Insert an instance of JPA annotated class into its table. Generated id is set if any.
     *
     * @param obj
     * @throws SQLException
     */
    <T> void insert(T obj) throws SQLException;

    /**
     * Insert instances of JPA annotated class into their table. Generated ids are set if any.
     *
     * @param obj
     * @throws SQLException
     */
    <T> void insert(Collection<T> obj) throws SQLException;

    /**
     * Begins a generic insert query into the given table.
     *
     * @param table
     * @return
     * @throws SQLException
     */
    IInsertQuery insert(String table) throws SQLException;

}
