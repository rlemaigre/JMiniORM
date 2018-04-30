package org.jminiorm.mapping.type;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The default implementation returns all integer numbers as Long, timestamps as LocalDateTime and dates as LocalDate.
 */
public class DefaultJDBCTypeMapper implements IJDBCTypeMapper {

    @Override
    public Class<?> getJavaType(int jdbcType) {
        switch (jdbcType) {
        case Types.BIGINT:
            return Long.class;
        case Types.INTEGER:
            return Integer.class;
        case Types.SMALLINT:
        case Types.TINYINT:
            return Short.class;
        case Types.TIMESTAMP:
        case Types.TIMESTAMP_WITH_TIMEZONE:
            return LocalDateTime.class;
        case Types.DATE:
            return LocalDate.class;
        default:
            return null;
        }
    }

}
