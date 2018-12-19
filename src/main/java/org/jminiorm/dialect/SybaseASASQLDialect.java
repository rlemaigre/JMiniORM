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
