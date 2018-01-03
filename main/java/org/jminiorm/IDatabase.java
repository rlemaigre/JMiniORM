package org.jminiorm;

import org.jminiorm.query.IDeleteQuery;
import org.jminiorm.query.IInsertQuery;
import org.jminiorm.query.ISelectQuery;
import org.jminiorm.query.IUpdateQuery;
import org.jminiorm.query.jpa.IJPASelectQuery;

import java.sql.SQLException;
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

    /************************************************************************************/
    /************************************ INSERT ****************************************/
    /************************************************************************************/

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
     * @param objs
     * @throws SQLException
     */
    <T> void insert(Collection<T> objs) throws SQLException;

    /**
     * Begins a generic insert query into the given table.
     *
     * @param table
     * @return
     * @throws SQLException
     */
    IInsertQuery insert(String table) throws SQLException;

    /************************************************************************************/
    /************************************ UPDATE ****************************************/
    /************************************************************************************/

    /**
     * Updates an instance of JPA annotated class.
     *
     * @param obj
     * @throws SQLException
     */
    <T> void update(T obj) throws SQLException;

    /**
     * Updates instances of JPA annotated class.
     *
     * @param objs
     * @throws SQLException
     */
    <T> void update(Collection<T> objs) throws SQLException;

    /**
     * Begins a generic update query on the given table.
     *
     * @param table
     * @param idColumn
     * @return
     * @throws SQLException
     */
    IUpdateQuery update(String table, String idColumn) throws SQLException;

    /************************************************************************************/
    /************************************ DELETE ****************************************/
    /************************************************************************************/

    /**
     * Deletes an instance of JPA annotated class.
     *
     * @param obj
     * @throws SQLException
     */
    <T> void delete(T obj) throws SQLException;

    /**
     * Deletes instances of JPA annotated class.
     *
     * @param objs
     * @throws SQLException
     */
    <T> void delete(Collection<T> objs) throws SQLException;

    /**
     * Begins a generic delete query on the given table.
     *
     * @param table
     * @param idColumn
     * @return
     * @throws SQLException
     */
    IDeleteQuery delete(String table, String idColumn) throws SQLException;

    /************************************************************************************/
    /************************************* OTHER ****************************************/
    /************************************************************************************/

    /**
     * Creates the table for the given JPA annotated class.
     *
     * @param clazz
     * @param <T>
     * @throws SQLException
     */
    <T> void createTable(Class<T> clazz) throws SQLException;

    /**
     * Executes an arbitrary SQL statement that returns nothing (UPDATE, DELETE, INSERT, CREATE TABLE, etc.) when other
     * provided methods are not sufficient.
     *
     * @param sql
     * @param params
     * @throws SQLException
     */
    void sql(String sql, Object... params) throws SQLException;

}
