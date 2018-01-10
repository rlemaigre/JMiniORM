package org.jminiorm.mapping.provider;

import org.jminiorm.mapping.JPAORMapping;
import org.jminiorm.mapping.ORMapping;

/**
 * A mapping provider that uses JPA annotations.
 */
public class JPAORMappingProvider extends AbstractORMappingProvider {

    @Override
    protected ORMapping createORMapping(Class<?> clazz) {
        return new JPAORMapping(clazz);
    }

}
