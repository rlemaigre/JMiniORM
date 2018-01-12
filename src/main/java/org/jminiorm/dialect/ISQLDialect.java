package org.jminiorm.dialect;

import org.jminiorm.mapping.ORMapping;

import java.util.List;

/**
 * Interface of objects responsible for turing the data that defines queries into executable SQL statements. The
 * implementation is database-specific.
 */
public interface ISQLDialect {

    String sqlForDelete(String table, String idColumn);

    String sqlForInsert(String table, List<String> columns);

    String sqlForUpdate(String table, String idColumn, List<String> columns);

    String sqlForSelect(String sql, Long limit, Long offset);

    String sqlForSelect(List<String> columns, String table, String where, String orderBy);

    String sqlForCreateTable(ORMapping mapping);

    List<String> sqlForCreateIndexes(ORMapping mapping);
}
