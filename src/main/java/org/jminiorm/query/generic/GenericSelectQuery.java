package org.jminiorm.query.generic;

import java.util.Arrays;
import java.util.List;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;
import org.jminiorm.resultset.IMapResultSet;
import org.jminiorm.resultset.IObjectResultSet;
import org.jminiorm.resultset.IPrimitiveResultSet;
import org.jminiorm.resultset.MapResultSet;
import org.jminiorm.resultset.ObjectResultSet;
import org.jminiorm.resultset.PrimitiveResultSet;

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
    public IMapResultSet<Object> asMap() {
        return new MapResultSet<Object>(getQueryTarget(), getSQL(), params, Object.class);
    }

    @Override
    public <T> IMapResultSet<T> asMap(Class<T> type) {
        return new MapResultSet<T>(getQueryTarget(), getSQL(), params, type);
    }

    @Override
    public <T> IObjectResultSet<T> asObject(Class<T> clazz) {
        return new ObjectResultSet<>(getQueryTarget(), getSQL(), params, clazz);
    }

    protected String getSQL() {
        return getQueryTarget().getConfig().getDialect().sqlForSelect(sql, limit, offset);
    }

}
