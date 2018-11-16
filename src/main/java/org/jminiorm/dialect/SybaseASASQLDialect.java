package org.jminiorm.dialect;

public class SybaseASASQLDialect extends GenericSQLDialect {

    @Override
    public SetNullParameterMethod getSetNullParameterMethod() {
        return SetNullParameterMethod.SETNULL;
    }

    protected String sqlForAutoIncrement() {
        return "DEFAULT AUTOINCREMENT";
    }

}
