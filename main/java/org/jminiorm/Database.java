package org.jminiorm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jminiorm.dialect.ANSISQLDialect;
import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.exception.DBException;
import org.jminiorm.executor.DefaultStatementExecutor;
import org.jminiorm.executor.IStatementExecutor;
import org.jminiorm.mapping.provider.IORMappingProvider;
import org.jminiorm.mapping.provider.JPAORMappingProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database extends AbstractQueryTarget implements IDatabase {

    private DataSource dataSource;
    private ISQLDialect dialect;
    private IORMappingProvider mappingProvider;
    private IStatementExecutor statementExecutor;

    public Database() {
    }

    /**
     * Creates a new database with a Hikary connection pool, ANSI SQL dialect, JPA mapping provider and default
     * statement executor.
     *
     * @param url
     * @param username
     * @param password
     */
    public Database(String url, String username, String password) {
        this();
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        setDataSource(new HikariDataSource(config));
        setDialect(new ANSISQLDialect());
        setORMappingProvider(new JPAORMappingProvider());
        setStatementExecutor(new DefaultStatementExecutor());
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ISQLDialect getDialect() {
        return dialect;
    }

    public void setDialect(ISQLDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public IORMappingProvider getORMappingProvider() {
        return mappingProvider;
    }

    public void setORMappingProvider(IORMappingProvider mappingProvider) {
        this.mappingProvider = mappingProvider;
    }

    public IStatementExecutor getStatementExecutor() {
        return statementExecutor;
    }

    public void setStatementExecutor(IStatementExecutor statementExecutor) {
        this.statementExecutor = statementExecutor;
    }

    @Override
    public Connection getConnection() throws DBException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void releaseConnection(Connection con) throws DBException {
        try {
            con.close();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public ITransaction createTransaction() throws DBException {
        return new Transaction(this);
    }

}
