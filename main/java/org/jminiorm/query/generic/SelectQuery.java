package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;
import org.jminiorm.resultset.IResultSet;

import java.util.Arrays;
import java.util.List;

public class SelectQuery extends AbstractQuery implements ISelectQuery {

    private String sql;
    private List<Object> params;
    private Long limit;
    private Long offset;

    public SelectQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public ISelectQuery sql(String sql, Object... params) {
        this.sql = sql;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public ISelectQuery limit(Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public ISelectQuery offset(Long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public <T> IResultSet<T> as(Class<T> clazz) {
        // TODO
        return null;
    }
}
