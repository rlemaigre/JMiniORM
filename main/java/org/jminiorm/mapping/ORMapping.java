package org.jminiorm.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the mapping between a Java class and a database table.
 */
public class ORMapping {

    private Class<?> javaClass;
    private String table;
    private List<Index> indexes;
    private List<ColumnMapping> columnMappings;
    private Map<String, ColumnMapping> columnMappingsIndexedByProperty;
    private Map<String, ColumnMapping> columnMappingsIndexedByColumn;
    private ColumnMapping idColumnMapping;

    public ORMapping() {
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public List<ColumnMapping> getColumnMappings() {
        return columnMappings;
    }

    public void setColumnMappings(List<ColumnMapping> columnMappings) {
        this.columnMappings = columnMappings;
    }

    /**
     * Returns the column mapping for the given property. Case doesn't matter.
     *
     * @param property
     * @return
     */
    public ColumnMapping getColumnMappingByProperty(String property) {
        if (columnMappingsIndexedByProperty == null) {
            columnMappingsIndexedByProperty = createColumnMappingsIndexedByProperty();
        }
        return columnMappingsIndexedByProperty.get(property.toLowerCase());
    }

    protected Map<String, ColumnMapping> createColumnMappingsIndexedByProperty() {
        Map<String, ColumnMapping> mappings = new HashMap<>();
        for (ColumnMapping mapping : getColumnMappings()) {
            mappings.put(mapping.getPropertyDescriptor().getName().toLowerCase(), mapping);
        }
        return mappings;
    }

    /**
     * Returns the column mapping for the given column. Case doesn't matter.
     *
     * @param column
     * @return
     */
    public ColumnMapping getColumnMappingByColumn(String column) {
        if (columnMappingsIndexedByColumn == null) {
            columnMappingsIndexedByColumn = createColumnMappingsIndexedByColumn();
        }
        return columnMappingsIndexedByColumn.get(column.toLowerCase());
    }

    protected Map<String, ColumnMapping> createColumnMappingsIndexedByColumn() {
        Map<String, ColumnMapping> mappings = new HashMap<>();
        for (ColumnMapping mapping : getColumnMappings()) {
            mappings.put(mapping.getColumn().toLowerCase(), mapping);
        }
        return mappings;
    }

    /**
     * Returns the column mapping for the property marked as id.
     *
     * @return
     */
    public ColumnMapping getIdColumnMapping() {
        if (idColumnMapping == null) {
            for (ColumnMapping mapping : getColumnMappings()) {
                if (mapping.isId()) {
                    idColumnMapping = mapping;
                    break;
                }
            }
            if (idColumnMapping == null)
                throw new RuntimeException("No ID column defined in class " + getJavaClass().getName());
        }
        return idColumnMapping;
    }
}
