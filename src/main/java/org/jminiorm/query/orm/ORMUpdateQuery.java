package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.*;
import java.util.stream.Collectors;

public class ORMUpdateQuery<T> extends AbstractORMQuery<T> implements IORMUpdateQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMUpdateQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMUpdateQuery<T> forClass(Class<T> clazz) {
        return (IORMUpdateQuery<T>) super.forClass(clazz);
    }

    @Override
    public ORMUpdateQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public ORMUpdateQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (!objs.isEmpty()) {
            // The table to update :
            String table = getMapping().getTable();

            // The maps column => value to update :
            List<Map<String, Object>> rows = new ArrayList<>();
            for (T obj : objs) {
                Map<String, Object> row = new HashMap<>();
                for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                    row.put(columnMapping.getColumn(), columnMapping.readProperty(obj));
                }
                rows.add(row);
            }

            // Update rows :
            getQueryTarget().update(table).schema(getMapping().getSchema())
                    .idColumns(getMapping().getIdColumnMappings().stream().map(ColumnMapping::getColumn).toArray(String[]::new))
                    .addMany(rows)
                    .execute();
        }
    }

}
