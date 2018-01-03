package org.jminiorm.query;

import java.util.List;

/**
 * Represents a generic insert query, that is, one that insert rows in an arbitrary table.
 */
public interface IInsertQuery {

    IInsertQuery table(String table);

    IInsertQuery columns(List<String> cols);

    IInsertQuery add(List<Object> values);

}
