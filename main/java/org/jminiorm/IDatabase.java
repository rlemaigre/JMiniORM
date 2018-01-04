package org.jminiorm;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.generic.IDeleteQuery;
import org.jminiorm.query.generic.IInsertQuery;
import org.jminiorm.query.generic.ISelectQuery;
import org.jminiorm.query.generic.IUpdateQuery;
import org.jminiorm.query.orm.IORMSelectQuery;

import java.sql.Connection;
import java.util.Collection;

public interface IDatabase {

    /************************************************************************************/
    /************************************ SELECT ****************************************/
    /************************************************************************************/

    /**
     * Returns the instance of the JPA annotated class T of given id.
     *
     * @param clazz
     * @param id
     * @param <T>
     * @return
     * @throws DBException
     */
    <T> T select(Class<T> clazz, Object id) throws DBException;

    /**
     * Begins a JPA select query.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws DBException
     */
    <T> IORMSelectQuery<T> select(Class<T> clazz);

    /**
     * Begins a generic select query.
     *
     * @param sql    The whole SQL, except limit and offset.
     * @param params
     * @return
     */
    ISelectQuery select(String sql, Object... params);

    /************************************************************************************/
    /************************************ INSERT ****************************************/
    /************************************************************************************/

    /**
     * Insert an instance of JPA annotated class into its table. Generated id is set if any.
     *
     * @param obj
     * @throws DBException
     */
    <T> void insert(T obj) throws DBException;

    /**
     * Insert instances of a JPA annotated class into their table. Generated ids are set if any.
     *
     * @param objs
     * @throws DBException
     */
    <T> void insert(Collection<T> objs) throws DBException;

    /**
     * Begins a generic insert query into the given table.
     *
     * @param table
     * @return
     * @throws DBException
     */
    IInsertQuery insert(String table) throws DBException;

    /************************************************************************************/
    /************************************ UPDATE ****************************************/
    /************************************************************************************/

    /**
     * Updates an instance of JPA annotated class.
     *
     * @param obj
     * @throws DBException
     */
    <T> void update(T obj) throws DBException;

    /**
     * Updates instances of JPA annotated class.
     *
     * @param objs
     * @throws DBException
     */
    <T> void update(Collection<T> objs) throws DBException;

    /**
     * Begins a generic update query on the given table.
     *
     * @param table
     * @return
     * @throws DBException
     */
    IUpdateQuery update(String table) throws DBException;

    /************************************************************************************/
    /************************************ DELETE ****************************************/
    /************************************************************************************/

    /**
     * Deletes an instance of a JPA annotated class by given id.
     *
     * @param clazz
     * @param id
     * @param <T>
     * @throws DBException
     */
    <T> void delete(Class<T> clazz, Object id) throws DBException;

    /**
     * Deletes an instance of JPA annotated class.
     *
     * @param obj
     * @throws DBException
     */
    <T> void delete(T obj) throws DBException;

    /**
     * Deletes instances of JPA annotated class.
     *
     * @param objs
     * @throws DBException
     */
    <T> void delete(Collection<T> objs) throws DBException;

    /**
     * Begins a generic delete query on the given table.
     *
     * @param table
     * @return
     * @throws DBException
     */
    IDeleteQuery delete(String table) throws DBException;

    /************************************************************************************/
    /************************************* OTHER ****************************************/
    /************************************************************************************/

    /**
     * Creates the table for the given JPA annotated class.
     *
     * @param clazz
     * @param <T>
     * @throws DBException
     */
    <T> void createTable(Class<T> clazz) throws DBException;

    /**
     * Executes an arbitrary SQL statement that returns nothing (UPDATE, DELETE, INSERT, CREATE TABLE, etc.) when other
     * provided methods are not sufficient.
     *
     * @param sql
     * @param params
     * @throws DBException
     */
    void sql(String sql, Object... params) throws DBException;

    /**
     * Get a (or THE for a transaction) connection to the current database. For a database this connection is in
     * auto-commit mode, for a transaction it's not.
     *
     * @return
     * @throws DBException
     */
    Connection getConnection() throws DBException;

    /**
     * This should ALWAYS be called after a connection is no longer in use. For a database, this closes the connection
     * and returns it to the pool. For a transaction, does nothing (the connection is closed when the transaction is).
     *
     * @param con
     * @throws DBException
     */
    void releaseConnection(Connection con) throws DBException;

}
