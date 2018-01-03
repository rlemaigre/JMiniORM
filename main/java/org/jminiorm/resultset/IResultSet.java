package org.jminiorm.resultset;

import org.jminiorm.exception.UnexpectedNumberOfItemsException;

import java.sql.SQLException;
import java.util.List;

/**
 * Represents a result set composed of objects of type T.
 *
 * @param <T>
 */
public interface IResultSet<T> {

    /**
     * Extracts the first result of the result set. Throws an exception if there is more than or less than one element
     * in the result set.
     *
     * @return
     * @throws UnexpectedNumberOfItemsException when there are more than one or zero items in the result set.
     * @throws SQLException
     */
    T one() throws UnexpectedNumberOfItemsException, SQLException;

    /**
     * Extracts the first result of the result set.
     *
     * @return
     * @throws SQLException
     */
    T first() throws SQLException;

    /**
     * Returns all the items in the result set.
     *
     * @return
     * @throws SQLException
     */
    List<T> list() throws SQLException;

}
