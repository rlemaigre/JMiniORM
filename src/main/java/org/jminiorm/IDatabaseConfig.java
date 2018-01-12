package org.jminiorm;

import org.jminiorm.dialect.ISQLDialect;
import org.jminiorm.executor.IStatementExecutor;
import org.jminiorm.mapping.provider.IORMappingProvider;
import org.jminiorm.mapping.type.IJDBCTypeMapper;

import javax.sql.DataSource;

public interface IDatabaseConfig {

    DataSource getDataSource();

    void setDataSource(DataSource dataSource);

    ISQLDialect getDialect();

    void setDialect(ISQLDialect dialect);

    IORMappingProvider getORMappingProvider();

    void setORMappingProvider(IORMappingProvider mappingProvider);

    IJDBCTypeMapper getJDBCTypeMapper();

    void setJDBCTypeMapper(IJDBCTypeMapper mapper);

    IStatementExecutor getStatementExecutor();

    void setStatementExecutor(IStatementExecutor executor);

}
