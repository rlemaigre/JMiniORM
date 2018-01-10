package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimitiveResultSet<T> extends AbstractResultSet<T> implements IPrimitiveResultSet<T> {

    private Class<T> targetClass;

    public PrimitiveResultSet(IQueryTarget queryTarget, String sql, List<Object> params, Class<T> targetClass) {
        super(queryTarget, sql, params);
        this.targetClass = targetClass;
    }

    @Override
    protected T castRow(Map<String, Object> row) {
        if (row.size() != 1)
            throw new RuntimeException("Cannot convert the result set into a primitive result set : some rows have " +
                    "more than one column.");
        return (T) row.values().iterator().next();
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put(null, targetClass);
        return mappings;
    }
}
