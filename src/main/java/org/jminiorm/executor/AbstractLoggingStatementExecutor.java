package org.jminiorm.executor;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

import java.util.List;
import java.util.Map;

/**
 * Subclass to implement logging of SQL statements and parameters.
 */
public abstract class AbstractLoggingStatementExecutor implements IStatementExecutor {

    private IStatementExecutor wrapped;

    /**
     * Pass the statement executor that needs to be decorated with logging functionality.
     *
     * @param wrapped
     */
    public AbstractLoggingStatementExecutor(IStatementExecutor wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public List<Long> executeUpdate(IQueryTarget target, String sql, List<List<Object>> params) throws DBException {
        logUpdate(target, sql, params);
        return wrapped.executeUpdate(target, sql, params);
    }

    protected void logUpdate(IQueryTarget target, String sql, List<List<Object>> params) throws DBException {
        log("SQL : " + sql);
        for (List<Object> cur : params) {
            log("Parameters : " + cur.toString());
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(IQueryTarget target, String sql, List<Object> params,
                                                  Map<String, Class<?>> typeOverrides) throws DBException {
        logQuery(target, sql, params, typeOverrides);
        return wrapped.executeQuery(target, sql, params, typeOverrides);
    }

    protected void logQuery(IQueryTarget target, String sql, List<Object> params,
                            Map<String, Class<?>> typeOverrides) throws DBException {
        log("SQL : " + sql);
        log("Parameters : " + params.toString());
    }

    /**
     * Implement to provide your own logging mechanism.
     *
     * @param message
     */
    protected abstract void log(String message);

}
