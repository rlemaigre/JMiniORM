package org.jminiorm.mapping.provider;

import org.jminiorm.mapping.ORMapping;

import javax.persistence.Index;
import javax.persistence.Table;
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


        return mapping;
    }

}
