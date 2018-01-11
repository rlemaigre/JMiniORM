package org.jminiorm;

import org.jminiorm.exception.DBException;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction extends AbstractQueryTarget implements ITransaction {

    private IDatabase database;
    private Connection connection;

    public Transaction(IDatabase database) throws DBException {
        this.database = database;
        connection = database.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public IDatabase getDatabase() {
        return database;
    }

    @Override
    public void commit() throws DBException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void rollback() throws DBException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void close() throws DBException {
        try {
            rollback();
        } catch (Throwable t) {
        }
        database.releaseConnection(connection);
    }

    @Override
    public Connection getConnection() throws DBException {
        return connection;
    }

    @Override
    public void releaseConnection(Connection con) throws DBException {
    }

    @Override
    public IDatabaseConfig getConfig() {
        return getDatabase().getConfig();
    }

}
