package org.jminiorm.dialect;

public class SybaseASASQLDialect extends GenericSQLDialect {

    @Override
    public SetNullParameterMethod getSetNullParameterMethod() {
        return SetNullParameterMethod.SETNULL;
    }

    protected String sqlForAutoIncrement() {
        return "DEFAULT AUTOINCREMENT";
    }

    @Override
    public String sqlForCreateSchema(String schema) {
        // SybaseASA does not support the common concept of schema, however a schema is auto-created par user, with an identical name
        // the user created is never used for login so the password has not to be known
        // as no IF NOT EXISTS is available, it is better not to use it for the moment
        // return "CREATE USER " + identifier(schema, false) + " IDENTIFIED BY " + quoteIdentifier(UUID.randomUUID().toString());
        return null;
    }

    @Override
    protected String sqlForColumnType(Class<?> javaType, Integer length, Integer scale, Integer precision,
                                      boolean generated) {
        if ((javaType == Boolean.class) || (javaType == boolean.class))
            return "INTEGER";
        else return super.sqlForColumnType(javaType, length, scale, precision, generated);
    }

    @Override
    public String sqlForSelect(String sql, Integer limit, Integer offset) {
        String expr = "";
        if (limit != null)
            expr = expr + " TOP " + limit + " ";
        if (offset != null)
            expr = expr + " START AT " + (offset + 1) + " ";
        sql = sql.trim();
        sql = sql.replaceAll("(?i)^select ", "select " + expr);
        return sql;
    }
}
