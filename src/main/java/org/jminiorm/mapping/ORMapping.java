package org.jminiorm.mapping;

import org.jminiorm.exception.DBException;
import org.jminiorm.utils.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the mapping between a Java class and a database table.
 */
public class ORMapping {

    private Class<?> javaClass;
    private String schema;
    private String table;
    private List<Index> indexes;
    private List<ColumnMapping> columnMappings;
    private CaseInsensitiveMap<ColumnMapping> columnMappingsIndexedByProperty;
    private CaseInsensitiveMap<ColumnMapping> columnMappingsIndexedByColumn;
    private List<ColumnMapping> idColumnMappings;
    private Boolean hasId;

    public ORMapping() {
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
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
        return columnMappingsIndexedByProperty.get(property);
    }

    protected CaseInsensitiveMap<ColumnMapping> createColumnMappingsIndexedByProperty() {
        CaseInsensitiveMap<ColumnMapping> mappings = new CaseInsensitiveMap<>();
        for (ColumnMapping mapping : getColumnMappings()) {
            mappings.put(mapping.getPropertyDescriptor().getName(), mapping);
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
        return columnMappingsIndexedByColumn.get(column);
    }

    protected CaseInsensitiveMap<ColumnMapping> createColumnMappingsIndexedByColumn() {
        CaseInsensitiveMap<ColumnMapping> mappings = new CaseInsensitiveMap<>();
        for (ColumnMapping mapping : getColumnMappings()) {
            mappings.put(mapping.getColumn(), mapping);
        }
        return mappings;
    }

    /**
     * Returns the column mappings for the properties marked as id.
     */
    public List<ColumnMapping> getIdColumnMappings() {
        if (idColumnMappings == null) {
            idColumnMappings = new ArrayList<>();
            for (ColumnMapping mapping : getColumnMappings()) {
                if (mapping.isId()) {
                    idColumnMappings.add( mapping);
                    break;
                }
            }
            if (idColumnMappings.isEmpty())
                throw new RuntimeException("No ID column defined in class " + getJavaClass().getName());
        }
        return idColumnMappings;
    }

    public ColumnMapping getIdColumnMapping() {
        if (getIdColumnMappings().size() > 1) throw new DBException("More than one ID column.");
        return getIdColumnMappings().get(0);
    }

    public Boolean hasId() {
        if (hasId == null) {
            hasId = getColumnMappings().stream().anyMatch(ColumnMapping::isId);
        }
        return hasId;
    }
}
