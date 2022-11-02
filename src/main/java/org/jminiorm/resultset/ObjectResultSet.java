package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;
import org.jminiorm.attributeconverter.AttributeConverterUtils;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.ORMapping;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
            Constructor<?> constructor = Arrays.stream(targetClass.getConstructors()).min(Comparator.comparing(Constructor::getParameterCount)).get();
            List<Object> args = new ArrayList<>();
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                args.add(canonicalValue(parameterType));
            }
            T obj = (T) constructor.newInstance(args.toArray());
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                columnMapping.writeProperty(obj, row.get(columnMapping.getColumn().replace("\"", "")));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Object canonicalValue(Class<?> clazz) {
        if (clazz == Long.class || clazz == Integer.class || clazz == Short.class || clazz == Double.class || clazz == Float.class ||
                clazz == int.class || clazz == long.class ||clazz == float.class || clazz == double.class || clazz == short.class)
            return 0;
        else if (clazz == String.class)
            return "";
        else if (clazz == BigDecimal.class)
            return BigDecimal.ZERO;
        else if (clazz == LocalDate.class)
            return LocalDate.now();
        else if (clazz == LocalDateTime.class)
            return LocalDateTime.now();
        else if (clazz == Boolean.class || clazz == boolean.class)
            return false;
        else
            return null;
    }

    protected ORMapping getMapping() {
        return getQueryTarget().getConfig().getORMappingProvider().getORMapping(targetClass);
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        Map<String, Class<?>> typeMappings = new HashMap<>();
        for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
            Class<?> propertyType;
            if (columnMapping.getConverter() != null) {
                propertyType = AttributeConverterUtils.getConverterDatabaseType(columnMapping.getConverter());
            } else
                propertyType = columnMapping.getPropertyDescriptor().getPropertyType();
            typeMappings.put(columnMapping.getColumn(), propertyType);
        }
        return typeMappings;
    }
}
