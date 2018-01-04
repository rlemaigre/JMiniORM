package org.jminiorm.resultset;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.ORMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResultSet<T> implements IResultSet<T> {

    private IQueryTarget queryTarget;
    private Class<T> clazz;
    private java.sql.ResultSet jdbcResultSet;

    public ResultSet(IQueryTarget queryTarget, java.sql.ResultSet jdbcResultSet) {
        this.queryTarget = queryTarget;
        this.jdbcResultSet = jdbcResultSet;
    }

    @Override
    public void forClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T one() throws UnexpectedNumberOfItemsException, DBException {
        return null;
    }

    @Override
    public T first() throws DBException {
        return null;
    }

    @Override
    public List<T> list() throws DBException {
        return null;
    }

    /**
     * Returns the next row from the JDBC result set.
     *
     * @return
     */
    protected Map<String, Object> nextRow() {

    }

    protected T buildObject(Map<String, Object> row) {
            ORMapping mapping = queryTarget.getORMappingProvider().getORMapping(clazz);
            if (mapping != null) {
                // An object relational mapping is defined for the target class :
                T obj = null;
                try {
                    obj = clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (ColumnMapping columnMapping : mapping.getColumnMappings()) {
                    columnMapping.writeProperty(obj, row.get(columnMapping.getColumn()));
                }
                return obj;
            } else if (Map.class.isAssignableFrom(clazz)) {
                // The target class implements Map :
                Map m = null;
                try {
                    m = (Map) clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                m.putAll(row);
                return (T) m;
            } else if (clazz == Boolean.class ||
                    clazz == Byte.class ||
                    clazz == Character.class ||
                    clazz == Integer.class ||
                    clazz == Float.class ||
                    clazz == Double.class ||
                    clazz == Long.class ||
                    clazz == Short.class ||
                    clazz == Date.class) {
                if (row.size() == 1) {
                    // Return the value as is :
                    return (T) row.values().iterator().next();
                } else {
                    throw new RuntimeException("");
                }
            } else {
                throw new RuntimeException("The target class of the result set must either be a JPA annotated class, " +
                        "a Map or a primitive type. Found : " + clazz.getName());
            }
    }

}
