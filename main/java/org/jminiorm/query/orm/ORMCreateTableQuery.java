package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

public class ORMCreateTableQuery<T> extends AbstractQuery implements IORMCreateTableQuery<T> {

    private Class<T> clazz;

    public ORMCreateTableQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMCreateTableQuery<T> forClass(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }

}
