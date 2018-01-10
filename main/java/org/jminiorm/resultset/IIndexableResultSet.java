package org.jminiorm.resultset;

import org.jminiorm.exception.DBException;

import java.util.List;
import java.util.Map;

/**
 * Common interface of object and map result sets.
 */
public interface IIndexableResultSet<T> {

    /**
     * Returns all the items in the result set as a Map. For each distinct value of the given column/property in the
     * result set, adds an entry to the map with that value as key and the list of rows/objects that match it as value.
     *
     * @return
     * @throws DBException
     */
    <K> Map<K, List<T>> group(String column) throws DBException;

    /**
     * Returns all the items in the result set as a Map. Throws an exception if the same value for the given
     * column/property is found more than once. Otherwise, adds an entry to the Map for each distinct value of the given
     * column/property and the corresponding row as value.
     *
     * @return
     * @throws DBException
     */
    <K> Map<K, T> index(String column) throws DBException;

}
