package org.jminiorm.mapping;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Populates the mapping using JPA annotations.
 */
public class JPAORMapping extends ORMapping {

    public JPAORMapping(Class<?> clazz) {
        super();

        // Java class :
        setJavaClass(clazz);

        // Table and indexes :
        Table tableAnn = clazz.getAnnotation(Table.class);
        if (tableAnn != null) {
            setTable(tableAnn.name());
            List<Index> indexes = new ArrayList<>();
            javax.persistence.Index[] indexesAnn = tableAnn.indexes();
            for (javax.persistence.Index indexAnn : indexesAnn) {
                org.jminiorm.mapping.Index index = new org.jminiorm.mapping.Index();
                index.setName(indexAnn.name());
                index.setUnique(indexAnn.unique());
                index.setColumns(indexAnn.columnList());
                indexes.add(index);
            }
            setIndexes(indexes);
        }

        // Columns :
        List<ColumnMapping> columnMappings = new ArrayList<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if (descriptor.getName().equals("class")) continue;
            ColumnMapping columnMapping = new ColumnMapping();
            columnMapping.setPropertyDescriptor(descriptor);
            Field field = getField(clazz, descriptor.getName());
            Transient transientAnn = field.getAnnotation(Transient.class);
            if (transientAnn != null) continue;
            Id idAnn = field.getAnnotation(Id.class);
            Column columnAnn = field.getAnnotation(Column.class);
            GeneratedValue generatedValueAnn = field.getAnnotation(GeneratedValue.class);
            Convert convertAnn = field.getAnnotation(Convert.class);
            columnMapping.setId(idAnn != null);
            columnMapping.setGenerated(generatedValueAnn != null);
            try {
                columnMapping.setConverter(convertAnn != null ? (AttributeConverter) convertAnn.converter()
                        .newInstance() : null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (columnAnn != null) {
                columnMapping.setColumn(columnAnn.name().equals("") ? descriptor.getName() : columnAnn.name());
                columnMapping.setColumnDefinition(columnAnn.columnDefinition().equals("") ? null : columnAnn
                        .columnDefinition());
                columnMapping.setInsertable(columnAnn.insertable());
                columnMapping.setLength(columnAnn.length() >= 255 ? null : columnAnn.length());
                columnMapping.setScale(columnAnn.scale());
                columnMapping.setNullable(columnAnn.nullable());
                columnMapping.setUpdatable(columnAnn.updatable());
                columnMapping.setPrecision(columnAnn.precision());
            } else {
                columnMapping.setColumn(descriptor.getName());
                columnMapping.setColumnDefinition(null);
                columnMapping.setInsertable(true);
                columnMapping.setLength(null);
                columnMapping.setScale(null);
                columnMapping.setNullable(true);
                columnMapping.setUpdatable(true);
                columnMapping.setPrecision(null);
            }
            columnMappings.add(columnMapping);
        }
        setColumnMappings(columnMappings);
    }

    /**
     * Returns the Field object corresponding of the given property name or null if none was found.
     *
     * @param clazz
     * @param name
     * @return
     */
    protected static Field getField(Class<?> clazz, String name) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ex) {
            }
            current = current.getSuperclass();
        }
        return null;
    }
}
