package org.jminiorm.mapping.type;

/**
 * When the database returns a result set, getObject() is used to extract values from each column. This interface can be
 * implemented to override the default Java type for a given JDBC type.
 */
public interface IJDBCTypeMapper {

    /**
     * Returns the Java type that must be used to represent the given JDBC type ({@link java.sql.Types}) or null if the
     * default must be used.
     *
     * @param jdbcType
     * @return
     */
    Class<?> getJavaType(int jdbcType);

}
