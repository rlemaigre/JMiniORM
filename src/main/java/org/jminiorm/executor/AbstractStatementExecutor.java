package org.jminiorm.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.utils.CaseInsensitiveMap;

public abstract class AbstractStatementExecutor implements IStatementExecutor {

    @Override
    public List<Map<String,Object>> executeQuery(IQueryTarget target, String sql, List<Object> params,
            Map<String,Class<?>> typeOverrides) throws DBException {
        CaseInsensitiveMap<Class<?>> caseInsensitiveTypeOverrides = new CaseInsensitiveMap<>(typeOverrides);
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Get raw ResultSet :
            con = target.getConnection();
            stmt = con.prepareStatement(sql);
            setParameters(stmt, params);
            rs = stmt.executeQuery();

            // Convert ResultSet to a list of maps :
            List<Map<String,Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String,Object> row = new CaseInsensitiveMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colName = metaData.getColumnName(i);
                    Class<?> type = caseInsensitiveTypeOverrides.get(colName);
                    if (type == null) type = caseInsensitiveTypeOverrides.get(null);
                    if (type == null)
                        row.put(colName, rs.getObject(i));
                    else {
                        row.put(colName, getObject(rs, metaData, i, type));
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

    protected void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 1; i <= params.size(); i++) {
            stmt.setObject(i, params.get(i - 1));
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
    protected Object getObject(ResultSet rs, ResultSetMetaData metaData, int columnIndex, Class<?> type)
            throws DBException {
        try {
            if (type == Object.class)
                return rs.getObject(columnIndex);
            else if (type == String.class)
                return rs.getString(columnIndex);
            else if (type == Integer.class || type == int.class)
                return rs.getInt(columnIndex);
            else if (type == BigDecimal.class)
                return rs.getBigDecimal(columnIndex);
            else if (type == Boolean.class || type == boolean.class)
                return rs.getBoolean(columnIndex);
            else if (type == Byte[].class || type == byte[].class)
                return rs.getBytes(columnIndex);
            else if (type == Date.class) {
                Timestamp timestamp = rs.getTimestamp(columnIndex);
                return timestamp == null ? null : new Date(timestamp.getTime());
            } else if (type == Double.class || type == double.class)
                return rs.getDouble(columnIndex);
            else if (type == Float.class || type == float.class)
                return rs.getFloat(columnIndex);
            else if (type == Long.class || type == long.class)
                return rs.getLong(columnIndex);
            else if (type == Short.class || type == short.class)
                return rs.getShort(columnIndex);
            else
                return rs.getObject(columnIndex, type);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

}
