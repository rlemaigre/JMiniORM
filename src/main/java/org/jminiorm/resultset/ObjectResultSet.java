package org.jminiorm.resultset;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jminiorm.IQueryTarget;
import org.jminiorm.attributeconverter.AttributeConverterUtils;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.ORMapping;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return getQueryTarget().getConfig().getORMappingProvider().getORMapping(targetClass);
    }

    @Override
    protected Map<String, Class<?>> typeMappings() {
        Map<String, Class<?>> typeMappings = new HashMap<>();
        for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
            Class<?> propertyType;
            if (columnMapping.getConverter() != null) {
                propertyType = AttributeConverterUtils.getConvertionResultType(columnMapping.getConverter());
            } else
                propertyType = columnMapping.getPropertyDescriptor().getPropertyType();
            typeMappings.put(columnMapping.getColumn(), propertyType);
        }
        return typeMappings;
    }
}
