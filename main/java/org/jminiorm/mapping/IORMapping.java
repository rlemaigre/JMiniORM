package org.jminiorm.mapping;

import java.util.List;

/**
 * Represents the mapping between a Java class and its underlying table.
 */
public interface IORMapping {

    Class<?> getJavaClass();

    String getTable();

    List<Index> getIndexes();

    List<ColumnMapping> getColumnMappings();

}
