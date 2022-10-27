package org.jminiorm.mapping;

import javax.persistence.AttributeConverter;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents the mapping between a java property and a database column.
 */
public class ColumnMapping {

    private PropertyDescriptor propertyDescriptor;
    private String columnDefinition;
    private boolean isId;
    private boolean insertable;
    private Integer length; // Null means no limit.
    private String column;
    private boolean nullable;
    private Integer precision;
    private Integer scale;
    private boolean updatable;
    private boolean generated;
    private AttributeConverter converter;

    public ColumnMapping() {
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public String getColumnDefinition() {
        return columnDefinition;
    }

    public void setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(boolean id) {
        isId = id;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public AttributeConverter<?, ?> getConverter() {
        return converter;
    }

    public void setConverter(AttributeConverter<?, ?> converter) {
        this.converter = converter;
    }

    /**
     * Utility method that reads the value of the property this mapping is about.
     *
     * @param bean
     * @return
     */
    public Object readProperty(Object bean) {
        try {
            Object value = getPropertyDescriptor().getReadMethod().invoke(bean);
            return converter == null ? value : converter.convertToDatabaseColumn(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method that writes the value of the property this mapping is about.
     *
     * @param bean
     * @return
     */
    public void writeProperty(Object bean, Object value) {
        try {
            Object converted = converter == null ? value : converter.convertToEntityAttribute(value);
            Method setter = getPropertyDescriptor().getWriteMethod();
            if (setter != null) {
                setter.invoke(bean,
                        converted
                );
            } else {
                String name = getPropertyDescriptor().getName();
                Field f = getField(bean.getClass(), name);
                f.setAccessible(true);
                f.set(bean, converted);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getField(Class mClass, String fieldName) throws NoSuchFieldException {
        try {
            return mClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = mClass.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
}
