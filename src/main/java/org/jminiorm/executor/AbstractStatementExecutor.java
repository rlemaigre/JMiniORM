package org.jminiorm.executor;

import org.jminiorm.IQueryTarget;
import org.jminiorm.dialect.SetNullParameterMethod;
import org.jminiorm.exception.DBException;
import org.jminiorm.utils.CaseInsensitiveMap;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractStatementExecutor implements IStatementExecutor {

    @Override
    public List<Map<String, Object>> executeQuery(IQueryTarget target, String sql, List<Object> params,
                                                  Map<String, Class<?>> typeOverrides) throws DBException {
        CaseInsensitiveMap<Class<?>> caseInsensitiveTypeOverrides = new CaseInsensitiveMap<>(typeOverrides);
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Get raw ResultSet :
            con = target.getConnection();
            stmt = con.prepareStatement(sql);
            setParameters(target, stmt, params);
            rs = stmt.executeQuery();

            // Convert ResultSet to a list of maps :
            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new CaseInsensitiveMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colName = metaData.getColumnName(i);
                    int jdbcType = metaData.getColumnType(i);
                    Class<?> type = caseInsensitiveTypeOverrides.get(colName);
                    if (type == null) type = caseInsensitiveTypeOverrides.get(null);
                    if (type == null) type = target.getConfig().getJDBCTypeMapper().getJavaType(jdbcType);
                    if (type == null)
                        row.put(colName, rs.getObject(i));
                    else {
                        row.put(colName, getObject(target, rs, metaData, i, type));
                    }
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (con != null) target.releaseConnection(con);
            } catch (Exception e) {
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
            }
        }
    }

    protected void setParameters(IQueryTarget target, PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 1; i <= params.size(); i++) {
            setObject(target, stmt, i, params.get(i - 1));
        }
    }

    /**
     * The method tries to use the JDBC 4.0 methods as much as possible, only using JDBC 4.1 getObject() as a last
     * resort, because it's not supported by some drivers (notably Sybase JConnect 7.07).
     *
     * @param rs
     * @param metaData
     * @param columnIndex
     * @param type
     * @return
     * @throws DBException
     */
    protected Object getObject(IQueryTarget target, ResultSet rs, ResultSetMetaData metaData, int columnIndex,
                               Class<?> type)
            throws DBException {
        try {
            Object result;
            if (type == Object.class)
                result = rs.getObject(columnIndex);
            else if (type == String.class)
                result = rs.getString(columnIndex);
            else if (type == Integer.class || type == int.class)
                result = rs.getInt(columnIndex);
            else if (type == BigDecimal.class)
                result = rs.getBigDecimal(columnIndex);
            else if (type == Boolean.class || type == boolean.class)
                result = rs.getBoolean(columnIndex);
            else if (type == Byte[].class || type == byte[].class)
                result = rs.getBytes(columnIndex);
            else if (type == Date.class) {
                Timestamp timestamp = rs.getTimestamp(columnIndex);
                result = timestamp == null ? null : new Date(timestamp.getTime());
            } else if (type == Double.class || type == double.class)
                result = rs.getDouble(columnIndex);
            else if (type == Float.class || type == float.class)
                result = rs.getFloat(columnIndex);
            else if (type == Long.class || type == long.class)
                result = rs.getLong(columnIndex);
            else if (type == Short.class || type == short.class)
                result = rs.getShort(columnIndex);
            else if (type == LocalDate.class) {
                Date d = (Date) getObject(target, rs, metaData, columnIndex, Date.class);
                if (d == null) result = null;
                else {
                    // Ensures that 01/01/1001 in database gets converted to 01/01/1001 localdate.
                    result = new java.sql.Date(d.getTime()).toLocalDate();
                }
            } else if (type == LocalDateTime.class) {
                Date d = (Date) getObject(target, rs, metaData, columnIndex, Date.class);
                if (d == null) result = null;
                else {
                    // Ensures that 01/01/1001 in database gets converted to 01/01/1001 localdate.
                    result = new java.sql.Timestamp(d.getTime()).toLocalDateTime();
                }
            } else
                result = rs.getObject(columnIndex, type);
            if (rs.wasNull())
                return null;
            else
                return result;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    protected void setObject(IQueryTarget target, PreparedStatement stmt, int columnIndex, Object param)
            throws DBException {
        try {
            if (param == null) {
                if (target.getConfig().getDialect().getSetNullParameterMethod() == SetNullParameterMethod.SETNULL)
                    stmt.setNull(columnIndex, Types.INTEGER);
                else
                    stmt.setObject(columnIndex, null);
            } else if (param instanceof String)
                stmt.setString(columnIndex, (String) param);
            else if (param instanceof Integer)
                stmt.setInt(columnIndex, (Integer) param);
            else if (param instanceof BigDecimal)
                stmt.setBigDecimal(columnIndex, (BigDecimal) param);
            else if (param instanceof Boolean)
                stmt.setBoolean(columnIndex, (Boolean) param);
            else if (param instanceof byte[])
                stmt.setBytes(columnIndex, (byte[]) param);
            else if (param instanceof Date) {
                Timestamp timestamp = new Timestamp(((Date) param).getTime());
                stmt.setTimestamp(columnIndex, timestamp);
            } else if (param instanceof Double)
                stmt.setDouble(columnIndex, (Double) param);
            else if (param instanceof Float)
                stmt.setFloat(columnIndex, (Float) param);
            else if (param instanceof Long)
                stmt.setLong(columnIndex, (Long) param);
            else if (param instanceof Short)
                stmt.setShort(columnIndex, (Short) param);
            else if (param instanceof LocalDate) {
                LocalDate ld = (LocalDate) param;
                java.sql.Date d = java.sql.Date.valueOf(ld);
                stmt.setDate(columnIndex, d);
            } else if (param instanceof LocalDateTime) {
                LocalDateTime ldt = (LocalDateTime) param;
                Timestamp timestamp = Timestamp.valueOf(ldt);
                stmt.setTimestamp(columnIndex, timestamp);
            } else
                stmt.setObject(columnIndex, param);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Returns the index of the generated column in the generated keys result set.
     *
     * @param rs
     * @param generatedColumn
     * @return
     * @throws SQLException
     */
    protected int getGeneratedColumnIndex(IQueryTarget target, ResultSet rs, String generatedColumn)
            throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        if (metaData.getColumnCount() == 1)
            return 1;
        else {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (metaData.getColumnName(i).equalsIgnoreCase(generatedColumn))
                    return i;
            }
            throw new DBException("Generated column " + generatedColumn + " not found in generated keys result set.");
        }
    }

}
