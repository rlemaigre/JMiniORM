package org.jminiorm.mapping;

import java.beans.PropertyDescriptor;

/**
 * Represents the mapping between a java property and a database column.
 */
public class ColumnMapping {

    private PropertyDescriptor propertyDescriptor;
    private String columnDefinition;
    private boolean insertable;
    private int length;
    private String column;
    private boolean nullable;
    private int precision;
    private int scale;
    private boolean updatable;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
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

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }
}
