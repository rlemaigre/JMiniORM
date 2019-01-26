package org.jminiorm;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.generic.IGenericDeleteQuery;
import org.jminiorm.query.generic.IGenericInsertQuery;
import org.jminiorm.query.generic.IGenericSelectQuery;
import org.jminiorm.query.generic.IGenericUpdateQuery;
import org.jminiorm.query.orm.IORMDeleteQuery;
import org.jminiorm.query.orm.IORMInsertQuery;
import org.jminiorm.query.orm.IORMSelectQuery;
import org.jminiorm.query.orm.IORMUpdateQuery;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IQueryTarget {

    /************************************************************************************/
    /************************************ SELECT ****************************************/
    /************************************************************************************/

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
     * Begins a generic select query.
     *
     * @param sql    The whole SQL, except limit and offset.
     * @param params
     * @return
     */
    IGenericSelectQuery select(String sql, Object... params);

    /************************************************************************************/
    /************************************ INSERT ****************************************/
    /************************************************************************************/

    /**
     * Begins a JPA insert query.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws DBException
     */
    <T> IORMInsertQuery<T> insert(Class<T> clazz);

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
    IGenericInsertQuery insert(String table) throws DBException;

    /************************************************************************************/
    /************************************ UPDATE ****************************************/
    /************************************************************************************/

    /**
     * Begins a JPA update query.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws DBException
     */
    <T> IORMUpdateQuery<T> update(Class<T> clazz);

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
    IGenericUpdateQuery update(String table) throws DBException;

    /************************************************************************************/
    /************************************ DELETE ****************************************/
    /************************************************************************************/

    /**
     * Begins a JPA delete query.
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws DBException
     */
    <T> IORMDeleteQuery<T> delete(Class<T> clazz);

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
    IGenericDeleteQuery delete(String table) throws DBException;

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
     * Drops the table for the given JPA annotated class.
     *
     * @param clazz
     * @param <T>
     * @throws DBException
     */
    <T> void dropTable(Class<T> clazz) throws DBException;

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
     * Executes the given SQL statement once for every set of parameters. Returns the generated keys if any.
     *
     * @param sql
     * @param params
     * @param generatedColumn
     * @return
     * @throws DBException
     */
    List<Long> executeUpdate(String sql, List<List<Object>> params, String generatedColumn) throws DBException;

    /**
     * Executes the given SQL statement with the given set of parameters and returns the rows. Column values for each
     * row are those returned by the JDBC driver as-is, unless a specific Java type is provided. In that case a
     * conversion will be attempted. If the conversion isn't supported by the driver, a DBException occurs. See {@link
     * java.sql.ResultSet#getObject(String, Class)}. The rows are returned as case-insensitive linked hash maps.
     *
     * @param sql
     * @param params
     * @param typeOverrides
     * @return
     * @throws DBException
     */
    List<Map<String, Object>> executeQuery(String sql, List<Object> params,
                                           Map<String, Class<?>> typeOverrides) throws DBException;

    /**
     * Returns a connection to the current database. Close, commit and rollback shouldn't be called on this connection.
     * Instead, call releaseConnection(con) when you're finished using it.
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

    /**
     * Returns the config.
     *
     * @return
     */
    IDatabaseConfig getConfig();

}
