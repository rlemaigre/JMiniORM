package org.jminiorm.mapping;

/**
 * Interface of objects responsible for creating and returning object-relational mappings to the ORM. Mappings should be
 * cached.
 */
public interface IORMappingProvider {

    /**
     * Returns the mapping for the given class.
     *
     * @param clazz
     * @return
     */
    ORMapping getORMapping(Class<?> clazz);

}
