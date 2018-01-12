package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;
import org.jminiorm.resultset.*;

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
    public <T> IPrimitiveResultSet<T> asPrimitive(Class<T> clazz) {
        return new PrimitiveResultSet<>(getQueryTarget(), getSQL(), params, clazz);
    }

    @Override
    public IMapResultSet asMap() {
        return new MapResultSet(getQueryTarget(), getSQL(), params);
    }

    @Override
    public <T> IObjectResultSet<T> asObject(Class<T> clazz) {
        return new ObjectResultSet<>(getQueryTarget(), getSQL(), params, clazz);
    }

    protected String getSQL() {
        return getQueryTarget().getConfig().getDialect().sqlForSelect(sql, limit, offset);
    }

}
