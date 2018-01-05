package org.jminiorm.dialect;

import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ORMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation that tries to be compatible with most databases. Subclass as needed to provide support for specific
 * databases.
 */
public class GenericSQLDialect implements ISQLDialect {

    @Override
    public final String sqlForDelete(String table, String idColumn) {
        return sqlForDeleteIdEscaped(identifier(table), identifier(idColumn));
    }

    protected String sqlForDeleteIdEscaped(String table, String idColumn) {
        return "DELETE FROM " + table + "\n" +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    public final String sqlForInsert(String table, List<String> columns) {
        return sqlForInsertIdEscaped(identifier(table), identifiers(columns));
    }

    protected String sqlForInsertIdEscaped(String table, List<String> columns) {
        return "INSERT INTO " + table + " (" + String.join(", ", columns) + ")\n" +
                "VALUES (" + String.join(", ", questionMarks(columns.size())) + ")";
    }

    @Override
    public final String sqlForUpdate(String table, String idColumn, List<String> columns) {
        return sqlForUpdateIdEscaped(identifier(table), identifier(idColumn), identifiers(columns));
    }

    protected String sqlForUpdateIdEscaped(String table, String idColumn, List<String> columns) {
        return "UPDATE " + table + "\n" +
                "SET " + String.join(" = ?, ", columns) + " = ?\n" +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    public String sqlForSelect(String sql, Long limit, Long offset) {
        return sql + " LIMIT " + limit + " OFFSET " + offset;
    }

    @Override
    public final String sqlForSelect(List<String> columns, String table, String where, String orderBy) {
        return sqlForSelectIdEscaped(identifiers(columns), identifier(table), where, orderBy);
    }

    public String sqlForSelectIdEscaped(List<String> columns, String table, String where, String orderBy) {
        return "SELECT " + String.join(", ", columns) + "\n" +
                "FROM " + table + "\n" +
                (where == null ? "" : ("WHERE " + where + "\n")) +
                (orderBy == null ? "" : ("ORDER BY " + orderBy + "\n"));
    }

    @Override
    public String sqlForCreateTable(ORMapping mapping) {
        String sql = "CREATE TABLE " + identifier(mapping.getTable()) + " (" +
                ")";
        return sql;
    }

    @Override
    public String sqlForCreateIndexes(ORMapping mapping) {
        String sql = "CREATE TABLE " + identifier(mapping.getTable()) + " (";
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
    protected String identifier(String identifier) {
        return quoteIdentifier(escapeIdentifier(identifier));
    }

    /**
     * Quotes the identifier.
     *
     * @param identifier
     * @return
     */
    protected String quoteIdentifier(String identifier) {
        return "\"" + identifier + "\"";
    }

    /**
     * To avoid SQL injection attacks, the default implementation simply throws an Exception if there is a special
     * character in the identifier. Only letters, numbers and underscores are allowed. Override if there is a safe
     * mechanism to escape special characters in your database and if you need special characters. You may also keep the
     * current behavior but whitelist some identifiers.
     *
     * @param identifier
     * @return
     * @throws DBException
     */
    protected String escapeIdentifier(String identifier) {
        if (!identifier.matches("^[a-zA-Z0-9_]+$"))
            throw new RuntimeException("Special character found in identifier '" + identifier + "'. To prevent SQL " +
                    "injection attacks, only letters, numbers and underscores are allowed in identifiers. Overrides " +
                    "escapeIdentifier in your SQL dialect if you need a different behavior.");
        else
            return identifier;
    }

}
