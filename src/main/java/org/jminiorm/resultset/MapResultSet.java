package org.jminiorm.resultset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jminiorm.IQueryTarget;

public class MapResultSet<V> extends AbstractResultSet<Map<String,V>> implements IMapResultSet<V> {

    private Class<V> type;

    public MapResultSet(IQueryTarget queryTarget, String sql, List<Object> params, Class<V> type) {
        super(queryTarget, sql, params);
        this.type = type;
    }

    @Override
    protected Map<String,V> castRow(Map<String,Object> row) {
        return (Map)row;
    }

    @Override
    protected Map<String,Class<?>> typeMappings() {
        Map<String,Class<?>> mappings = new HashMap<>();
        mappings.put(null, type);
        return mappings;
    }
}
