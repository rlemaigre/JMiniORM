package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.*;

public class ORMDeleteQuery<T> extends AbstractORMQuery<T> implements IORMDeleteQuery<T> {

    private List<List<Object>> ids = new ArrayList<>();
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
    public IORMDeleteQuery<T> id(Object... id) {
        ids.add(Arrays.asList(id));
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addOne(T obj) {
        id(getMapping().getIdColumnMappings().stream().map(cm -> cm.readProperty(obj)).toArray());
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addMany(Collection<T> objs) {
        for (T obj : objs) {
            addOne(obj);
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
        String schema = getMapping().getSchema();

        if (!ids.isEmpty()) {
            ColumnMapping idColumnMapping = getMapping().getIdColumnMapping();
            getQueryTarget().delete(table).schema(schema)
                    .idColumn(idColumnMapping.getColumn())
                    .addMany((List)ids)
                    .execute();
        }
        if (where != null) {
            getQueryTarget().delete(table).schema(schema).where(where, params.toArray()).execute();
        }
    }
}
