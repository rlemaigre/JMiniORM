package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.query.generic.IGenericSelectQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ORMSelectQuery<T> extends AbstractORMQuery<T> implements IORMSelectQuery<T> {

    private String where;
    private List<Object> params;
    private Long limit;
    private Long offset;
    private String orderBy;

    public ORMSelectQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMSelectQuery<T> forClass(Class<T> clazz) {
        return (IORMSelectQuery<T>) super.forClass(clazz);
    }

    @Override
    public IORMSelectQuery<T> where(String where, Object... params) {
        this.where = where;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public IORMSelectQuery<T> id(Object id) {
        // Sets the parameters as the id :
        params = new ArrayList<>();
        params.add(id);

        // Sets the where clause for the id column :
        where = getMapping().getIdColumnMapping().getColumn() + " = ?";

        return this;
    }

    @Override
    public IORMSelectQuery<T> limit(Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public IORMSelectQuery<T> offset(Long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public IORMSelectQuery<T> orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public T one() throws UnexpectedNumberOfItemsException, DBException {
        Map<String, Object> row = getGenericQuery().one();
        return buildObject(row);
    }

    @Override
    public T first() throws DBException {
        Map<String, Object> row = getGenericQuery().first();
        return buildObject(row);
    }

    @Override
    public List<T> list() throws DBException {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> rows = getGenericQuery().list();
        for (Map<String, Object> row : rows) {
            list.add(buildObject(row));
        }
        return list;
    }

    /**
     * Returns the generic query that gets the rows from the database.
     *
     * @return
     */
    protected IGenericSelectQuery getGenericQuery() {
        return getQueryTarget().select(getSQL(), params).limit(limit).offset(offset);
    }

    /**
     * Builds the SQL select to get the rows from the database.
     *
     * @return
     */
    protected String getSQL() {
        // The table to select from :
        String table = getMapping().getTable();

        // The columns to put in the select list :
        List<String> columns = new ArrayList<>();
        for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
            columns.add(columnMapping.getColumn());
        }

        // Generate sql :
        String sql = "SELECT " + String.join(", ", columns) + "\n" +
                "FROM " + table + "\n" +
                (where == null ? "" : ("WHERE " + where + "\n")) +
                (orderBy == null ? "" : ("ORDER BY " + orderBy + "\n"));

        return sql;
    }

    /**
     * Builds an instance of T from a database row.
     *
     * @param row
     * @return
     */
    protected T buildObject(Map<String, Object> row) {
        try {
            T obj = getTargetClass().newInstance();
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                columnMapping.writeProperty(obj, row.get(columnMapping.getColumn()));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
