package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ORMDeleteQuery<T> extends AbstractORMQuery<T> implements IORMDeleteQuery<T> {

    private List<Object> ids = new ArrayList<>();
    private String where;
    private List<Object> params = new ArrayList<>();

    public ORMDeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMDeleteQuery<T> forClass(Class<T> clazz) {
        return (IORMDeleteQuery<T>) super.forClass(clazz);
    }

    @Override
    public IORMDeleteQuery<T> id(Object id) {
        ids.add(id);
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addOne(T obj) {
        ids.add(getMapping().getIdColumnMapping().readProperty(obj));
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addMany(Collection<T> objs) {
        for (T obj : objs) {
            ids.add(getMapping().getIdColumnMapping().readProperty(obj));
        }
        return this;
    }

    @Override
    public IORMDeleteQuery<T> where(String where, Object... params) {
        this.where = where;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public void execute() throws DBException {
        // The table to delete rows from :
        String table = getMapping().getTable();

        if (!ids.isEmpty()) {
            ColumnMapping idColumnMapping = getMapping().getIdColumnMapping();
            getQueryTarget().delete(table)
                    .idColumn(idColumnMapping.getColumn())
                    .addMany(ids)
                    .execute();
        }
        if (where != null) {
            getQueryTarget().delete(table).where(where, params.toArray()).execute();
        }
    }
}
