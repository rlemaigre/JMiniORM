package org.jminiorm.dialect;

import org.jminiorm.mapping.ORMapping;

import java.util.List;

/**
 * An implementation that tries to be compatible with most databases. Subclass as needed to provide support for specific
 * databases.
 */
public class GenericSQLDialect implements ISQLDialect {

    @Override
    public String sqlForDelete(String table, String idColumn) {
        //TODO
        return null;
    }

    @Override
    public String sqlForInsert(String table, List<String> columns) {
        //TODO
        return null;
    }

    @Override
    public String sqlForUpdate(String table, String idColumn, List<String> columns) {
        //TODO
        return null;
    }

    @Override
    public String sqlForSelect(String sql, Long limit, Long offset) {
        //TODO
        return null;
    }

    @Override
    public String sqlForSelect(List<String> columns, String table, String where, String orderBy) {
        return "SELECT " + String.join(", ", columns) + "\n" +
                "FROM " + table + "\n" +
                (where == null ? "" : ("WHERE " + where + "\n")) +
                (orderBy == null ? "" : ("ORDER BY " + orderBy + "\n"));
    }

    @Override
    public String sqlForCreateTable(ORMapping mapping) {
        //TODO
        return null;
    }

}
