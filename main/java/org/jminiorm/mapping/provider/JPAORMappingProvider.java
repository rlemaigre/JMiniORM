package org.jminiorm.mapping.provider;

import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.ORMapping;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A mapping provider that uses JPA annotations.
 */
public class JPAORMappingProvider extends AbstractORMappingProvider {

    @Override
    protected ORMapping createORMapping(Class<?> clazz) {
        ORMapping mapping = new ORMapping();

        // Java class :
        mapping.setJavaClass(clazz);

        // Table name :
        Table tableAnn = clazz.getAnnotation(Table.class);
        mapping.setTable(tableAnn.name());

        // Indexes :
        List<org.jminiorm.mapping.Index> indexes = new ArrayList<>();
        Index[] indexesAnn = tableAnn.indexes();
        for (Index indexAnn : indexesAnn) {
            org.jminiorm.mapping.Index index = new org.jminiorm.mapping.Index();
            index.setName(indexAnn.name());
            index.setUnique(indexAnn.unique());
            index.setColumns(indexAnn.columnList());
            indexes.add(index);
        }
        mapping.setIndexes(indexes);

        // Columns :
        List<ColumnMapping> columnMappings = new ArrayList<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            ColumnMapping columnMapping = new ColumnMapping();
            columnMapping.setPropertyDescriptor(descriptor);
            Field field = getField(clazz, descriptor.getName());
            Id idAnn = field.getAnnotation(Id.class);
            Lob lobAnn = field.getAnnotation(Lob.class);
            Column columnAnn = field.getAnnotation(Column.class);
            columnMapping.setId(idAnn != null);
            if (columnAnn != null) {
                columnMapping.setColumn(columnAnn.name());
                columnMapping.setColumnDefinition(columnAnn.columnDefinition());
                columnMapping.setInsertable(columnAnn.insertable());
                columnMapping.setLength(lobAnn != null ? null : columnAnn.length());
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
        mapping.setColumnMappings(columnMappings);

        return mapping;
    }

    /**
     * Returns the Field object corresponding of the given property name or null if none was found.
     *
     * @param clazz
     * @param name
     * @return
     */
    protected static Field getField(Class<?> clazz, String name) {
        Field f;
        Class<?> current = clazz;
        while (current.getSuperclass() != null) {
            try {
                f = current.getDeclaredField(name);
                return f;
            } catch (NoSuchFieldException ex) {
            }
            current = current.getSuperclass();
        }
        return null;
    }

}
