package org.jminiorm.executor;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.utils.CaseInsensitiveMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The default implementation of IStatementExecutor.
 */
public class DefaultStatementExecutor implements IStatementExecutor {

    @Override
    public List<Long> executeUpdate(IQueryTarget target, String sql, List<List<Object>> params) throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            List<Long> generatedKeys = new ArrayList<>();
            con = target.getConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (List<Object> curParams : params) {
                setParameters(stmt, curParams);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    Long key = null;
                    if (rs != null && rs.next()) {
                        key = rs.getLong(1);
                    }
                    generatedKeys.add(key);
                }
            }
            return generatedKeys;
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
        }
    }

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
            setParameters(stmt, params);
            rs = stmt.executeQuery();

            // Convert ResultSet to a list of maps :
            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new CaseInsensitiveMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colName = metaData.getColumnName(i);
                    Class<?> type = caseInsensitiveTypeOverrides.get(colName);
                    if (type == null) type = caseInsensitiveTypeOverrides.get(null);
                    if (type == null)
                        row.put(colName, rs.getObject(i));
                    else {
                        if (type == java.util.Date.class) {
                            java.sql.Timestamp d = rs.getObject(i, java.sql.Timestamp.class);
                            row.put(colName, d == null ? null : new java.util.Date(d.getTime()));
                        } else row.put(colName, rs.getObject(i, type));
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

}
