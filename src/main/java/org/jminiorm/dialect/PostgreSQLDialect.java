package org.jminiorm.dialect;

public class PostgreSQLDialect extends GenericSQLDialect {

    @Override
    protected String sqlForAutoIncrement() {
        return "";
    }

    @Override
    protected String sqlForColumnType(Class<?> javaType, Integer length, Integer scale, Integer precision,
            boolean generated) {
        if (generated) return "SERIAL";
        else if (javaType == byte[].class) return "bytea";
        else return super.sqlForColumnType(javaType, length, scale, precision, generated);
    }

}
