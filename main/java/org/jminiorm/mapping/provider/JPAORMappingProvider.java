package org.jminiorm.mapping.provider;

import org.jminiorm.mapping.JPAORMapping;
import org.jminiorm.mapping.ORMapping;

import javax.persistence.Table;

/**
 * A mapping provider that uses JPA annotations.
 */
public class JPAORMappingProvider extends AbstractORMappingProvider {

    @Override
    protected ORMapping createORMapping(Class<?> clazz) {
        if (clazz.getAnnotation(Table.class) != null)
            return new JPAORMapping(clazz);
        else
            return null;
    }

}
