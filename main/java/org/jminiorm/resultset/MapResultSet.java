package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapResultSet extends AbstractResultSet<Map<String, Object>> implements IMapResultSet {

    public MapResultSet(IQueryTarget queryTarget, String sql, List<Object> params) {
        super(queryTarget, sql, params);
    }

    @Override
    protected Map<String, Object> castRow(Map<String, Object> row) {
        return row;
    }

    @Override
    public <K> Map<K, List<Map<String, Object>>> group(String column) throws DBException {
        List<Map<String, Object>> rs = list();
        return rs.stream().collect(Collectors.groupingBy(m -> (K) m.get(column)));
    }

    @Override
    public <K> Map<K, Map<String, Object>> index(String column) throws DBException {
        List<Map<String, Object>> rs = list();
        return rs.stream().collect(Collectors.toMap(m -> (K) m.get(column), Function.identity()));
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        return Collections.emptyMap();
    }
}
