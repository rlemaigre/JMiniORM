package org.jminiorm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jminiorm.dialect.GenericSQLDialect;
import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.exception.DBException;
import org.jminiorm.executor.DefaultStatementExecutor;
import org.jminiorm.executor.IStatementExecutor;
import org.jminiorm.mapping.provider.IORMappingProvider;
import org.jminiorm.mapping.provider.JPAORMappingProvider;
import org.jminiorm.mapping.type.DefaultJDBCTypeMapper;
import org.jminiorm.mapping.type.IJDBCTypeMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database extends AbstractQueryTarget implements IDatabase {

    private DataSource dataSource;
    private ISQLDialect dialect;
    private IORMappingProvider mappingProvider;
    private IStatementExecutor statementExecutor;
    private IJDBCTypeMapper jdbcTypeMapper;

    public Database() {
    }

    /**
     * Creates a new database with a Hikary connection pool.
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
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ISQLDialect getDialect() {
        if (dialect == null)
            dialect = new GenericSQLDialect();
        return dialect;
    }

    public void setDialect(ISQLDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public IORMappingProvider getORMappingProvider() {
        if (mappingProvider == null)
            mappingProvider = new JPAORMappingProvider();
        return mappingProvider;
    }

    public void setORMappingProvider(IORMappingProvider mappingProvider) {
        this.mappingProvider = mappingProvider;
    }

    @Override
    public IStatementExecutor getStatementExecutor() {
        if (statementExecutor == null)
            statementExecutor = new DefaultStatementExecutor();
        return statementExecutor;
    }

    public void setStatementExecutor(IStatementExecutor statementExecutor) {
        this.statementExecutor = statementExecutor;
    }

    @Override
    public IJDBCTypeMapper getJDBCTypeMapper() {
        if (jdbcTypeMapper == null)
            jdbcTypeMapper = new DefaultJDBCTypeMapper();
        return jdbcTypeMapper;
    }

    public void setJDBCTypeMapper(IJDBCTypeMapper jdbcTypeMapper) {
        this.jdbcTypeMapper = jdbcTypeMapper;
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
