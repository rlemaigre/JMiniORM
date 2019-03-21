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
    public List<Long> executeUpdate(IQueryTarget target, String sql, List<List<Object>> params, String generatedColumn)
            throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            List<Long> generatedKeys = new ArrayList<>();
            con = target.getConnection();
            if (generatedColumn != null)
                stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            else
                stmt = con.prepareStatement(sql);
            for (List<Object> curParams : params) {
                setParameters(target, stmt, curParams);
                stmt.executeUpdate();
                if (generatedColumn != null) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        int generatedKeyIndex = getGeneratedColumnIndex(target, rs, generatedColumn);
                        rs.next();
                        generatedKeys.add(rs.getLong(generatedKeyIndex));
                    }
                }
            }
            return generatedKeys;
        } catch (SQLException e) {
            // throw new DBException(e);
            throw new DBException(sql + "\n" + e);
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
