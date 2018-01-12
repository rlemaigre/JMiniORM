package org.jminiorm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jminiorm.dialect.GenericSQLDialect;
import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.executor.DefaultStatementExecutor;
import org.jminiorm.executor.IStatementExecutor;
import org.jminiorm.mapping.provider.IORMappingProvider;
import org.jminiorm.mapping.provider.JPAORMappingProvider;
import org.jminiorm.mapping.type.DefaultJDBCTypeMapper;
import org.jminiorm.mapping.type.IJDBCTypeMapper;

import javax.sql.DataSource;

public class DatabaseConfig implements IDatabaseConfig {

    private DataSource dataSource;
    private ISQLDialect dialect;
    private IORMappingProvider mappingProvider;
    private IJDBCTypeMapper typeMapper;
    private IStatementExecutor executor;

    public DatabaseConfig() {
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ISQLDialect getDialect() {
        return dialect;
    }

    @Override
    public void setDialect(ISQLDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public IORMappingProvider getORMappingProvider() {
        return mappingProvider;
    }

    @Override
    public void setORMappingProvider(IORMappingProvider mappingProvider) {
        this.mappingProvider = mappingProvider;
    }

    @Override
    public IJDBCTypeMapper getJDBCTypeMapper() {
        return typeMapper;
    }

    @Override
    public void setJDBCTypeMapper(IJDBCTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public IStatementExecutor getStatementExecutor() {
        return executor;
    }

    @Override
    public void setStatementExecutor(IStatementExecutor executor) {
        this.executor = executor;
    }

    public static class Builder {

        private DataSource dataSource;
        private ISQLDialect dialect;
        private IORMappingProvider mappingProvider;
        private IJDBCTypeMapper typeMapper;
        private IStatementExecutor executor;

        public Builder() {
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder dataSource(String url, String username, String password) {
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(10);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            return dataSource(new HikariDataSource(config));
        }

        public Builder dialect(ISQLDialect dialect) {
            this.dialect = dialect;
            return this;
        }

        public Builder mappingProvider(IORMappingProvider mappingProvider) {
            this.mappingProvider = mappingProvider;
            return this;
        }

        public Builder typeMapper(IJDBCTypeMapper typeMapper) {
            this.typeMapper = typeMapper;
            return this;
        }

        public Builder statementExecutor(IStatementExecutor executor) {
            this.executor = executor;
            return this;
        }

        public DatabaseConfig build() {
            DatabaseConfig config = new DatabaseConfig();
            config.setDataSource(dataSource);
            if (dialect != null)
                config.setDialect(dialect);
            else
                config.setDialect(new GenericSQLDialect());
            if (mappingProvider != null)
                config.setORMappingProvider(mappingProvider);
            else
                config.setORMappingProvider(new JPAORMappingProvider());
            if (typeMapper != null)
                config.setJDBCTypeMapper(typeMapper);
            else
                config.setJDBCTypeMapper(new DefaultJDBCTypeMapper());
            if (executor != null)
                config.setStatementExecutor(executor);
            else
                config.setStatementExecutor(new DefaultStatementExecutor());
            return config;
        }

    }
}
