package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.*;

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

            // The column mappings for all updatable columns other than the id :
            List<ColumnMapping> relevantColumnMappings = new ArrayList<>();
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                if (!columnMapping.isId() && columnMapping.isUpdatable()) {
                    relevantColumnMappings.add(columnMapping);
                }
            }

            // The maps column => value to update :
            ColumnMapping idColumnMapping = getMapping().getIdColumnMapping();
            List<Map<String, Object>> rows = new ArrayList<>();
            for (T obj : objs) {
                Map<String, Object> row = new HashMap<>();
                row.put(idColumnMapping.getColumn(), idColumnMapping.readProperty(obj));
                for (ColumnMapping columnMapping : relevantColumnMappings) {
                    row.put(columnMapping.getColumn(), columnMapping.readProperty(obj));
                }
                rows.add(row);
            }

            // Update rows :
            getQueryTarget().update(table)
                    .idColumn(idColumnMapping.getColumn())
                    .addMany(rows)
                    .execute();
        }
    }

}
