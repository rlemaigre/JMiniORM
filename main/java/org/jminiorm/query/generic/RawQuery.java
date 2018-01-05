package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.Arrays;
import java.util.List;

public class RawQuery extends AbstractQuery implements IRawQuery {

    private String sql;
    private List<Object> params;

    public RawQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IRawQuery sql(String sql, Object... params) {
        this.sql = sql;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }
}
