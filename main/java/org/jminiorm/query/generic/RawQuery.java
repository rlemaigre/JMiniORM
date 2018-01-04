package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

public class RawQuery extends AbstractQuery implements IRawQuery {

    private String sql;

    public RawQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public void sql(String sql) {
        this.sql = sql;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }
}
