package org.jminiorm.mapping;

public interface IORMappingRegistry {

    /**
     * Returns the mapping for the given class.
     *
     * @param clazz
     * @return
     */
    IORMapping getORMapping(Class<?> clazz);

}
