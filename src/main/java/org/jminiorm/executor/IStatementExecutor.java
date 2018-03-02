package org.jminiorm.executor;

import java.util.List;
import java.util.Map;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

/**
 * Interface of objects responsible for executing SQL statements.
 */
public interface IStatementExecutor {

    /**
     * Executes the given SQL statement once for every set of parameters. Returns the generated keys if any.
     *
     * @param sql
     * @param params
     * @param generatedColumn
     * @return
     * @throws DBException
     */
    List<Long> executeUpdate(IQueryTarget target, String sql, List<List<Object>> params, String generatedColumn)
            throws DBException;

    /**
     * Executes the given SQL statement with the given set of parameters and returns the rows. Column values for each
     * row are those returned by the JDBC driver as-is, unless a specific Java type is provided. In that case a
     * conversion will be attempted. If the conversion isn't supported by the driver, a DBException occurs. See {@link
     * java.sql.ResultSet#getObject(String, Class)}. The rows are returned as case-insensitive linked hash maps.
     *
     * @param target
     * @param sql
     * @param params
     * @param typeOverrides
     * @return
     * @throws DBException
     */
    List<Map<String,Object>> executeQuery(IQueryTarget target, String sql, List<Object> params,
            Map<String,Class<?>> typeOverrides) throws DBException;

}
