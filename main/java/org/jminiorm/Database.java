package org.jminiorm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jminiorm.dialect.ANSISQLDialect;
import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.provider.IORMappingProvider;
import org.jminiorm.mapping.provider.JPAORMappingProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database extends AbstractQueryTarget implements IDatabase {

    private DataSource dataSource;
    private ISQLDialect dialect;
    private IORMappingProvider mappingProvider;

    public Database() {
    }

    /**
     * Creates a new database with a Hikary connection pool, ANSI SQL dialect and JPA mapping provider.
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
        return null;
    }

}
