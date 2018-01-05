package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMDeleteQuery<T> extends AbstractORMQuery<T> implements IORMDeleteQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMDeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMDeleteQuery<T> forClass(Class<T> clazz) {
        return (IORMDeleteQuery<T>) super.forClass(clazz);
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
            // The table to delete rows from :
            String table = getMapping().getTable();

            // The ids of the rows to delete :
            ColumnMapping idColumnMapping = getMapping().getIdColumnMapping();
            List<Object> ids = new ArrayList<>();
            for (T obj : objs) {
                ids.add(idColumnMapping.readProperty(obj));
            }

            // Delete rows :
            getQueryTarget().delete(table)
                    .idColumn(idColumnMapping.getColumn())
                    .addMany(ids)
                    .execute();
        }
    }
}
