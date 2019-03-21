package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

public class ORMCreateTableQuery<T> extends AbstractORMQuery<T> implements IORMCreateTableQuery<T> {

    public ORMCreateTableQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMCreateTableQuery<T> forClass(Class<T> clazz) {
        return (IORMCreateTableQuery<T>) super.forClass(clazz);
    }

    @Override
    public void execute() throws DBException {
        verifySchemaExistence();

        String sql = getQueryTarget().getConfig().getDialect().sqlForCreateTable(getMapping());
        getQueryTarget().sql(sql);
        for (String s : getQueryTarget().getConfig().getDialect().sqlForCreateIndexes(getMapping())) {
            getQueryTarget().sql(s);
        }
    }
}
