package org.jminiorm.dialect;

import org.jminiorm.mapping.ORMapping;

import java.util.List;

/**
 * An implementation that respects the SQL ANSI standard.
 */
public class ANSISQLDialect implements ISQLDialect {

    @Override
    public String sqlForDelete(String table, String idColumn) {
        return null;
    }

    @Override
    public String sqlForInsert(String table, List<String> columns) {
        return null;
    }

    @Override
    public String sqlForUpdate(String table, String idColumn, List<String> columns) {
        return null;
    }

    @Override
    public String sqlForSelect(String sql, Long limit, Long offset) {
        return null;
    }

    @Override
    public String sqlForCreateTable(ORMapping mapping) {
        return null;
    }

}
