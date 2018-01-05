package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.query.AbstractQuery;

import java.util.Arrays;
import java.util.List;

public class GenericSelectQuery extends AbstractQuery implements IGenericSelectQuery {

    private String sql;
    private List<Object> params;
    private Long limit;
    private Long offset;

    public GenericSelectQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericSelectQuery sql(String sql, Object... params) {
        this.sql = sql;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public IGenericSelectQuery limit(Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public IGenericSelectQuery offset(Long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public <T> T one() throws UnexpectedNumberOfItemsException, DBException {
        // TODO
        return null;
    }

    @Override
    public <T> T first() throws DBException {
        // TODO
        return null;
    }

    @Override
    public <T> List<T> list() throws DBException {
        // TODO
        return null;
    }

}
