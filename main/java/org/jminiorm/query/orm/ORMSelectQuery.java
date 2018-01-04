package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.resultset.IResultSet;

import java.util.*;

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
    public IORMSelectQuery<T> where(String where, Object... params) {
        this.where = where;
        this.params = Arrays.asList(params);
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
        Map<String,Object> row = getResultSet().one();
        return buildObject(row);
    }

    @Override
    public T first() throws DBException {
        Map<String,Object> row = getResultSet().first();
        return buildObject(row);
    }

    @Override
    public List<T> list() throws DBException {
        List<Map> rows = getResultSet().list();
        rows.get(0).get
    }

    protected IResultSet<HashMap> getResultSet() throws DBException {
        return getQueryTarget().select(getSQL(), params).limit(limit).offset(offset).as(HashMap.class);
    }

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


}
