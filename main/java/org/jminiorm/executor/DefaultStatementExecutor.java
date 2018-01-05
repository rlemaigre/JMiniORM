package org.jminiorm.executor;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.utils.CaseInsensitiveMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The default implementation of IStatementExecutor. Subclass to provide your own functionality (e.g. logging of
 * statements and parameters...).
 */
public class DefaultStatementExecutor implements IStatementExecutor {

    @Override
    public List<Object> executeUpdate(IQueryTarget target, String sql, List<List<Object>> params) throws DBException {
        Connection con = target.getConnection();
        try {

        } finally {
            target.releaseConnection(con);
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
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i, params.get(i));
            }
            rs = stmt.executeQuery();

            // Convert ResultSet to a list of maps :
            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new CaseInsensitiveMap<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String colName = metaData.getColumnName(i);
                    Class<?> type = caseInsensitiveTypeOverrides.get(colName);
                    if (type == null)
                        row.put(colName, rs.getObject(i));
                    else
                        row.put(colName, rs.getObject(i, type));
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

}
