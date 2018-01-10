package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractResultSet<T> implements IResultSet<T> {

    private IQueryTarget queryTarget;
    private String sql;
    private List<Object> params;

    public AbstractResultSet(IQueryTarget queryTarget, String sql, List<Object> params) {
        this.queryTarget = queryTarget;
        this.sql = sql;
        this.params = params;
    }

    @Override
    public T one() throws UnexpectedNumberOfItemsException, DBException {
        List<T> rs = list();
        if (rs.size() != 1) throw new UnexpectedNumberOfItemsException(rs.size());
        else return rs.get(0);
    }

    @Override
    public T first() throws DBException {
        List<T> rs = list();
        if (rs.isEmpty()) return null;
        else return rs.get(0);
    }

    @Override
    public List<T> list() throws DBException {
        List<Map<String, Object>> rawResultSet = queryTarget.executeQuery(sql, params, typeMappings());
        return rawResultSet.stream().map(this::castRow).collect(Collectors.toList());
    }

    protected IQueryTarget getQueryTarget() {
        return queryTarget;
    }

    /**
     * Cast the row into type T.
     *
     * @param row
     * @return
     */
    protected abstract T castRow(Map<String, Object> row);

    /**
     * Return the column name => Java type mappings to use when executing the query.
     *
     * @return
     */
    protected abstract Map<String, Class<?>> typeMappings();
}
