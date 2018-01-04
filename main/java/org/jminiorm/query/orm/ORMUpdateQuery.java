package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMUpdateQuery<T> extends AbstractORMQuery<T> implements IORMUpdateQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMUpdateQuery(IQueryTarget target) {
        super(target);
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
            String table = getMapping().getTable();
            String idColumn = getMapping().getIdColumnMapping().getColumn();
            List<String> columns = new ArrayList<>();
            List<ColumnMapping> relevantColumnMappings = new ArrayList<>();
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                if (!columnMapping.isId() && columnMapping.isUpdatable()) {
                    relevantColumnMappings.add(columnMapping);
                    columns.add(columnMapping.getColumn());
                }
            }
            List<Object> ids = new ArrayList<>();
            List<List<Object>> values = new ArrayList<>();
            for (T obj : objs) {
                ids.add(getMapping().getIdColumnMapping().readProperty(obj));
                List<Object> vals = new ArrayList<>();
                for (ColumnMapping columnMapping : relevantColumnMappings) {
                    vals.add(columnMapping.readProperty(obj));
                }
                values.add(vals);
            }
            getQueryTarget().update(table)
                    .idColumn(idColumn)
                    .columns(columns)
                    .addMany(ids, values)
                    .execute();
        }
    }

}
