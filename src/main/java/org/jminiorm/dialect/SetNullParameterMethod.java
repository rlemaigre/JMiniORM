package org.jminiorm.dialect;

public enum SetNullParameterMethod {
    /**
     * Uses stmt.setNull(..., Types.INTEGER). Works with Sybase.
     */
    SETNULL,
    /**
     * Uses stmt.setObject(..., null). Works with H2 and PostgreSQL.
     */
    SETOBJECT;
}