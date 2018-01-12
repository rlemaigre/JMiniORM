package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapResultSet extends AbstractResultSet<Map<String, Object>> implements IMapResultSet {

    public MapResultSet(IQueryTarget queryTarget, String sql, List<Object> params) {
        super(queryTarget, sql, params);
    }

    @Override
    protected Map<String, Object> castRow(Map<String, Object> row) {
        return row;
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        return Collections.emptyMap();
    }
}
