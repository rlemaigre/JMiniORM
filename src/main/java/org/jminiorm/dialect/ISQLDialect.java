package org.jminiorm.dialect;

import org.jminiorm.mapping.ORMapping;

import java.util.List;

/**
 * Interface of objects responsible for turing the data that defines queries into executable SQL statements. The
 * implementation is database-specific.
 */
public interface ISQLDialect {

    String sqlForDelete(String schema, String table, List<String> idColumns);

    String sqlForDeleteWhere(String schema, String table, String where);

    String sqlForInsert(String schema, String table, List<String> columns);

    String sqlForUpdate(String schema, String table, List<String> idColumns, List<String> columns);

    String sqlForSelect(String sql, Integer limit, Integer offset);

    String sqlForSelect(String schema, List<String> columns, String table, String where, String orderBy);

    String sqlForCreateTable(ORMapping mapping);

    String sqlForDropTable(ORMapping mapping);

    String sqlForCreateSchema(String schema);

    List<String> sqlForCreateIndexes(ORMapping mapping);

    SetNullParameterMethod getSetNullParameterMethod();
}
