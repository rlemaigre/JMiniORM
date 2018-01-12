package org.jminiorm.executor;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of IStatementExecutor.
 */
public class DefaultStatementExecutor extends AbstractStatementExecutor {

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

}
