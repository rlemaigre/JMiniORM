package org.jminiorm.dialect;

import org.jminiorm.attributeconverter.AttributeConverterUtils;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.Index;
import org.jminiorm.mapping.ORMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An implementation that tries to be compatible with most databases. Subclass as needed to provide support for specific
 * databases.
 */
public class GenericSQLDialect implements ISQLDialect {

    @Override
    public final String sqlForDelete(String schema, String table, String idColumn) {
        return sqlForDeleteIdEscaped(quoteIdentifier(schema), identifier(table), identifier(idColumn));
    }

    protected String sqlForDeleteIdEscaped(String schema, String table, String idColumn) {
        return "DELETE FROM " + schemaPrefix(schema) + table + "\n" +
                "WHERE " + idColumn + " = ?";
    }

    public final String sqlForDeleteWhere(String schema, String table, String where) {
        return sqlForDeleteWhereIdEscaped(quoteIdentifier(schema), identifier(table), where);
    }

    protected String sqlForDeleteWhereIdEscaped(String schema, String table, String where) {
        return "DELETE FROM " + schemaPrefix(schema) + table + "\n" +
                "WHERE " + where;
    }

    @Override
    public final String sqlForInsert(String schema, String table, List<String> columns) {
        return sqlForInsertIdEscaped(quoteIdentifier(schema), identifier(table), identifiers(columns));
    }

    protected String sqlForInsertIdEscaped(String schema, String table, List<String> columns) {
        return "INSERT INTO " + schemaPrefix(schema) + table + " (" + String.join(", ", columns) + ")\n" +
                "VALUES (" + String.join(", ", questionMarks(columns.size())) + ")";
    }

    @Override
    public final String sqlForUpdate(String schema, String table, String idColumn, List<String> columns) {
        return sqlForUpdateIdEscaped(quoteIdentifier(schema), identifier(table), identifier(idColumn), identifiers(columns));
    }

    protected String sqlForUpdateIdEscaped(String schema, String table, String idColumn, List<String> columns) {
        return "UPDATE " + schemaPrefix(schema) + table + "\n" +
                "SET " + String.join(" = ?, ", columns) + " = ?\n" +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    public String sqlForSelect(String sql, Integer limit, Integer offset) {
        if (limit != null)
            sql = sql + " LIMIT " + limit + " ";
        if (offset != null)
            sql = sql + " OFFSET " + offset + " ";
        return sql;
    }

    @Override
    public final String sqlForSelect(String schema, List<String> columns, String table, String where, String orderBy) {
        return sqlForSelectIdEscaped(quoteIdentifier(schema), identifiers(columns), identifier(table), where, orderBy);
    }

    protected String sqlForSelectIdEscaped(String schema, List<String> columns, String table, String where, String orderBy) {
        return "SELECT " + String.join(", ", columns) + "\n" +
                "FROM " + schemaPrefix(schema) + table + "\n" +
                (where == null ? "" : ("WHERE " + where + "\n")) +
                (orderBy == null ? "" : ("ORDER BY " + orderBy + "\n"));
    }


    @Override
    public String sqlForCreateSchema(String schema) {
        return "CREATE SCHEMA IF NOT EXISTS " + identifier(schema, true);
    }

    @Override
    public String sqlForDropTable(ORMapping mapping) {
        return "DROP TABLE IF EXISTS " + identifier(mapping.getSchema(), mapping.getTable());
    }

    @Override
    public String sqlForCreateTable(ORMapping mapping) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(identifier(mapping.getSchema(), mapping.getTable())).append(" (");
        List<String> columns = new ArrayList<>();
        for (ColumnMapping columnMapping : mapping.getColumnMappings()) {
            columns.add(sqlForColumnDefinition(columnMapping));
        }
        sb.append(String.join(", ", columns));
        if (mapping.getColumnMappings().stream().anyMatch(ColumnMapping::isId)) {
            sb.append(", ");
            sb.append(sqlForPrimaryKey(mapping, mapping.getIdColumnMapping()));
        }
        sb.append(")");
        return sb.toString();
    }

    protected String sqlForColumnDefinition(ColumnMapping columnMapping) {
        StringBuilder sb = new StringBuilder();
        sb.append(identifier(columnMapping.getColumn())).append(" ");
        if (columnMapping.getColumnDefinition() != null)
            sb.append(columnMapping.getColumnDefinition());
        else {
            sb.append(sqlForColumnType(columnMapping)).append(" ");
            if (!columnMapping.isNullable() || columnMapping.isId())
                sb.append("NOT NULL ");
            else
                sb.append("NULL ");
            if (columnMapping.isGenerated())
                sb.append(sqlForAutoIncrement());
        }
        return sb.toString();
    }

    protected String sqlForColumnType(ColumnMapping columnMapping) {
        Class<?> javaType;
        if (columnMapping.getConverter() != null) {
            javaType = AttributeConverterUtils.getConverterDatabaseType(columnMapping.getConverter());
        } else
            javaType = columnMapping.getPropertyDescriptor().getPropertyType();
        Integer length = columnMapping.getLength();
        Integer scale = columnMapping.getScale();
        Integer precision = columnMapping.getPrecision();
        boolean generated = columnMapping.isGenerated();
        return sqlForColumnType(javaType, length, scale, precision, generated);
    }

    protected String sqlForColumnType(Class<?> javaType, Integer length, Integer scale, Integer precision,
                                      boolean generated) {
        if ((javaType == Short.class) || (javaType == short.class))
            return "SMALLINT";
        if ((javaType == Integer.class) || (javaType == int.class))
            return "INTEGER";
        if ((javaType == Long.class) || (javaType == long.class))
            return "BIGINT";
        if ((javaType == Float.class) || (javaType == float.class))
            return "REAL";
        if ((javaType == Double.class) || (javaType == double.class))
            return "DOUBLE";
        if ((javaType == Boolean.class) || (javaType == boolean.class))
            return "BOOLEAN";
        if (javaType == byte[].class)
            return "BINARY";
        if (javaType == String.class) {
            if (length == null)
                return "TEXT";
            else
                return "VARCHAR(" + length + ")";
        }
        if (javaType == Date.class || javaType == LocalDateTime.class)
            return "TIMESTAMP";
        if (javaType == LocalDate.class)
            return "DATE";
        if (javaType == BigDecimal.class)
            return "NUMERIC(" + precision + "," + scale + ")";
        throw new RuntimeException("No SQL type defined in dialect for java type " + javaType.getName());
    }

    protected String sqlForAutoIncrement() {
        return "AUTO_INCREMENT";
    }

    protected String sqlForPrimaryKey(ORMapping mapping, ColumnMapping idColumnMapping) {
        return "CONSTRAINT " + identifier(mapping.getTable() + "_pk") + " PRIMARY KEY ("
                + identifier(idColumnMapping.getColumn()) + ")";
    }

    @Override
    public List<String> sqlForCreateIndexes(ORMapping mapping) {
        List<String> sqls = new ArrayList<>();
        for (Index index : mapping.getIndexes()) {
            sqls.add(sqlForIndex(mapping.getSchema(), index, mapping.getTable()));
        }
        return sqls;
    }

    protected String sqlForIndex(String schema, Index index, String table) {
        String name = index.getName();
        if (name == null || name.equals("")) {
            name = table + "__" + index.getColumns().replaceAll(" ", "").replaceAll(",", "_");
        }
        String sql = "CREATE " + (index.isUnique() ? "UNIQUE " : "") + "INDEX " + name + " ON " +
                identifier(schema, table) + " (" +
                index.getColumns() +
                ")";
        return sql;
    }

    /**
     * Produced a list of question marks.
     *
     * @param number
     * @return
     */
    protected List<String> questionMarks(int number) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < number; i++)
            res.add("?");
        return res;
    }

    /**
     * Escapes and quotes the identifiers.
     *
     * @param identifiers
     * @return
     */
    protected List<String> identifiers(List<String> identifiers) {
        List<String> result = new ArrayList<>();
        for (String id : identifiers) {
            result.add(identifier(id));
        }
        return result;
    }

    /**
     * Escapes and quotes the identifier.
     *
     * @param identifier
     * @return
     */
    protected String identifier(String identifier, boolean acceptNone) {
        if (acceptNone && (identifier == null || identifier.isEmpty())) return identifier;
        return quoteIdentifier(escapeIdentifier(identifier));
    }

    /**
     * Escapes and quotes the identifier.
     *
     * @param identifier
     * @return
     */
    protected String identifier(String identifier) {
        return identifier(identifier, false);
    }

    /**
     * Escapes and quotes the table identifier.
     *
     * @param identifier
     * @return
     */
    protected String identifier(String schema, String identifier) {
        return schemaPrefix(quoteIdentifier(schema)) + quoteIdentifier(escapeIdentifier(identifier));
    }

    /**
     * Quotes the identifier. Does nothing by default, so that you can leave identifiers unquoted in your statements as
     * well without having case-sensitivity problems with some databases.
     *
     * @param identifier
     * @return
     */
    protected String quoteIdentifier(String identifier) {
        return identifier;
    }

    /**
     * To avoid SQL injection attacks, the default implementation simply throws an Exception if there is a special
     * character in the identifier. Only letters, numbers, dots and underscores are allowed. Override if there is a safe
     * mechanism to escape special characters in your database and if you need special characters. You may also keep the
     * current behavior but whitelist some identifiers.
     *
     * @param identifier
     * @return
     * @throws DBException
     */
    protected String escapeIdentifier(String identifier) {
        if (!identifier.matches("^[a-zA-Z0-9_.]+$"))
            throw new RuntimeException("Special character found in identifier '" + identifier + "'. To prevent SQL " +
                    "injection attacks, only letters, numbers, dots and underscores are allowed in identifiers. " +
                    "Overrides escapeIdentifier in your SQL dialect if you need a different behavior.");
        else
            return identifier;
    }

    protected String schemaPrefix(String schema) {
        return (schema == null || schema.isEmpty()) ? "" : schema + ".";
    }

    @Override
    public SetNullParameterMethod getSetNullParameterMethod() {
        return SetNullParameterMethod.SETOBJECT;
    }

}
