package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.query.AbstractQuery;

import java.util.Arrays;
import java.util.List;

public class ORMSelectQuery<T> extends AbstractQuery implements IORMSelectQuery<T> {

    private Class<T> from;
    private String where;
    private List<Object> params;
    private Long limit;
    private Long offset;
    private String orderBy;

    public ORMSelectQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMSelectQuery<T> from(Class<T> clazz) {
        this.from = from;
        return this;
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
        // TODO
        return null;
    }

    @Override
    public T first() throws DBException {
        // TODO
        return null;
    }

    @Override
    public List<T> list() throws DBException {
        // TODO
        return null;
    }
}
