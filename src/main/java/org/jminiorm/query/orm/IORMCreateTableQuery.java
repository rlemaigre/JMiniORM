package org.jminiorm.query.orm;

import org.jminiorm.exception.DBException;

import java.sql.SQLException;

/**
 * Represents a create table statement that creates the table for a JPA annotated class.
 */
public interface IORMCreateTableQuery<T> extends IORMQuery<T> {

    @Override
    IORMCreateTableQuery<T> forClass(Class<T> clazz);

    /**
     * Creates the table.
     *
     * @throws SQLException
     */
    void execute() throws DBException;

}
