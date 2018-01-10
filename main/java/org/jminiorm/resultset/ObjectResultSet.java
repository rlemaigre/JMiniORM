package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.ORMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectResultSet<T> extends AbstractResultSet<T> implements IObjectResultSet<T> {

    private Class<T> targetClass;

    public ObjectResultSet(IQueryTarget queryTarget, String sql, List<Object> params,
                           Class<T> targetClass) {
        super(queryTarget, sql, params);
        this.targetClass = targetClass;
    }

    @Override
    protected T castRow(Map<String, Object> row) {
        try {
            T obj = targetClass.newInstance();
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                columnMapping.writeProperty(obj, row.get(columnMapping.getColumn()));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ORMapping getMapping() {
        return getQueryTarget().getORMappingProvider().getORMapping(targetClass);
    }

    @Override
    public <K> Map<K, List<T>> group(String property) throws DBException {
        List<T> rs = list();
        ColumnMapping columnMapping = getMapping().getColumnMappingByProperty(property);
        return rs.stream().collect(Collectors.groupingBy(obj -> (K) columnMapping.readProperty(obj)));
    }

    @Override
    public <K> Map<K, T> index(String property) throws DBException {
        List<T> rs = list();
        ColumnMapping columnMapping = getMapping().getColumnMappingByProperty(property);
        return rs.stream().collect(Collectors.toMap(obj -> (K) columnMapping.readProperty(obj), Function.identity()));
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        Map<String, Class<?>> typeMappings = new HashMap<>();
        for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
            Class<?> propertyType = columnMapping.getPropertyDescriptor().getPropertyType();
            typeMappings.put(columnMapping.getColumn(), propertyType);
        }
        return typeMappings;
    }
}
