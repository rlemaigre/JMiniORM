package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericRawQuery extends AbstractQuery implements IGenericRawQuery {

    private String sql;
    private List<Object> params;

    public GenericRawQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericRawQuery sql(String sql, Object... params) {
        this.sql = sql;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public void execute() throws DBException {
        List<List<Object>> params = new ArrayList<>();
        params.add(this.params);
        getQueryTarget().executeUpdate(sql, params);
    }
}
