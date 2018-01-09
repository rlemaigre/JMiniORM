package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.query.AbstractQuery;
import org.jminiorm.utils.CaseInsensitiveMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenericSelectQuery extends AbstractQuery implements IGenericSelectQuery {

    private String sql;
    private List<Object> params;
    private Long limit;
    private Long offset;
    private Map<String, Class<?>> typeMappings = new CaseInsensitiveMap<Class<?>>();

    public GenericSelectQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericSelectQuery sql(String sql, Object... params) {
        this.sql = sql;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public IGenericSelectQuery limit(Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public IGenericSelectQuery offset(Long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public IGenericSelectQuery type(String column, Class<?> javaClass) {
        typeMappings.put(column, javaClass);
        return this;
    }

    @Override
    public IGenericSelectQuery types(Map<String, Class<?>> typeMappings) {
        this.typeMappings.putAll(typeMappings);
        return this;
    }

    @Override
    public <T> T one() throws UnexpectedNumberOfItemsException, DBException {
        List<Map<String, Object>> rs = getResultSet();
        if (rs.size() != 1) throw new UnexpectedNumberOfItemsException(rs.size());
        else return rowToObject(rs.get(0));
    }

    @Override
    public <T> T first() throws DBException {
        List<Map<String, Object>> rs = getResultSet();
        if (rs.isEmpty()) return null;
        else return rowToObject(rs.get(0));
    }

    @Override
    public <T> List<T> list() throws DBException {
        List<Map<String, Object>> rs = getResultSet();
        return rs.stream().map(row -> (T) rowToObject(row)).collect(Collectors.toList());
    }

    @Override
    public <K> Map<K, List<Map<String, Object>>> index(String column) throws DBException {
        List<Map<String, Object>> rs = list();
        return rs.stream().collect(Collectors.groupingBy(m -> (K) m.get(column)));
    }

    @Override
    public <K> Map<K, Map<String, Object>> uniqueIndex(String column) throws DBException {
        List<Map<String, Object>> rs = list();
        return rs.stream().collect(Collectors.toMap(m -> (K) m.get(column), Function.identity()));
    }

    /**
     * Converts the row into an object. If there is only one pair in the map, the value is returned, otherwise the map
     * is returned.
     *
     * @param <T>
     * @return
     */
    protected static <T> T rowToObject(Map<String, Object> row) {
        if (row.size() == 1)
            return (T) row.entrySet().iterator().next().getValue();
        else
            return (T) row;
    }

    protected List<Map<String, Object>> getResultSet() throws DBException {
        return getQueryTarget().executeQuery(getSQL(), params, typeMappings);
    }

    protected String getSQL() {
        return getQueryTarget().getDialect().sqlForSelect(sql, limit, offset);
    }

}
