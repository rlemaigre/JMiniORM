package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ORMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMDeleteQuery<T> extends AbstractORMQuery<T> implements IORMDeleteQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMDeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMDeleteQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (!objs.isEmpty()) {
            ORMapping mapping = getMapping();
            String table = mapping.getTable();
            String idColumn = mapping.getIdColumnMapping().getColumn();
            List<Object> ids = new ArrayList<>();
            for (T obj : objs) {
                ids.add(mapping.getIdColumnMapping().readProperty(obj));
            }
            getQueryTarget().delete(table)
                    .idColumn(idColumn)
                    .addMany(ids)
                    .execute();
        }
    }
}
