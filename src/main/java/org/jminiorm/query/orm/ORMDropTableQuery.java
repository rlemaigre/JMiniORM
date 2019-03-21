package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

public class ORMDropTableQuery<T> extends AbstractORMQuery<T> implements IORMDropTableQuery<T> {

    public ORMDropTableQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMDropTableQuery<T> forClass(Class<T> clazz) {
        return (IORMDropTableQuery<T>) super.forClass(clazz);
    }

    @Override
    public void execute() throws DBException {
        verifySchemaExistence();

        String sql = getQueryTarget().getConfig().getDialect().sqlForDropTable(getMapping());
        getQueryTarget().sql(sql);
    }

}
