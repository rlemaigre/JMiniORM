package org.jminiorm.utils;

import org.jminiorm.exception.DBException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility functions for dealing with result set, either composed of maps or objects.
 */
public class RSUtils {

    /**
     * Returns all the items in the result set as a Map. For each distinct value of the given column/property in the
     * result set, adds an entry to the map with that value as key and the list of rows/objects that match it as value.
     *
     * @return
     * @throws DBException
     */
    static public <K, T> Map<K, List<T>> group(List<T> rs, String column) throws DBException {
        if (rs.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Class<T> clazz = (Class<T>) rs.get(0).getClass();
            IPropertyReader<T> reader = getReader(clazz, column);
            return rs.stream().collect(Collectors.groupingBy(obj -> (K) reader.readProperty(obj)));
        }
    }

    /**
     * Returns all the items in the result set as a Map. Throws an exception if the same value for the given
     * column/property is found more than once. Otherwise, adds an entry to the Map for each distinct value of the given
     * column/property and the corresponding row as value.
     *
     * @return
     * @throws DBException
     */
    static public <K, T> Map<K, T> index(List<T> rs, String column) throws DBException {
        if (rs.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Class<T> clazz = (Class<T>) rs.get(0).getClass();
            IPropertyReader<T> reader = getReader(clazz, column);
            return rs.stream().collect(Collectors.toMap(obj -> (K) reader.readProperty(obj), Function.identity()));
        }
    }

    /**
     * Returns all the items in the result set as a Map. Throws an exception if the same value for the given
     * column/property is found more than once. Otherwise, adds an entry to the Map for each distinct value of the given
     * column/property and the corresponding row as value.
     *
     * @return
     * @throws DBException
     */
    static public <K, T> Set<K> distinct(List<T> rs, String column) throws DBException {
        if (rs.isEmpty()) {
            return Collections.emptySet();
        } else {
            Class<T> clazz = (Class<T>) rs.get(0).getClass();
            IPropertyReader<T> reader = getReader(clazz, column);
            return rs.stream().map(obj -> (K) reader.readProperty(obj)).distinct().collect(Collectors.toSet());
        }
    }

    /**
     * Returns a reader for the given property of the given class.
     *
     * @param clazz
     * @param property
     * @param <T>
     * @return
     */
    static private <T> IPropertyReader<T> getReader(Class<T> clazz, String property) {
        if (Map.class.isAssignableFrom(clazz)) {
            return (IPropertyReader<T>) new MapPropertyReader(property);
        } else {
            return new ObjectPropertyReader<T>(clazz, property);
        }
    }

    interface IPropertyReader<T> {
        Object readProperty(T obj);
    }

    static class MapPropertyReader implements IPropertyReader<Map> {

        private String property;

        public MapPropertyReader(String property) {
            this.property = property;
        }

        @Override
        public Object readProperty(Map obj) {
            return obj.get(property);
        }

    }

    static class ObjectPropertyReader<T> implements IPropertyReader<T> {

        private Method reader;

        public ObjectPropertyReader(Class<T> clazz, String property) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                PropertyDescriptor descriptor = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(p -> p
                        .getName().equals(property)).findFirst().get();
                reader = descriptor.getReadMethod();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        @Override
        public Object readProperty(T obj) {
            try {
                return reader.invoke(obj);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

}
