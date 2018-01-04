package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

public class ORMCreateTableQuery<T> extends AbstractORMQuery<T> implements IORMCreateTableQuery<T> {

    public ORMCreateTableQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public void execute() throws DBException {
        String sql = getQueryTarget().getDialect().sqlForCreateTable(getMapping());
        getQueryTarget().sql(sql);
    }

}
