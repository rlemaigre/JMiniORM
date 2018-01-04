package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMInsertQuery<T> extends AbstractORMQuery<T> implements IORMInsertQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMInsertQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public ORMInsertQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public ORMInsertQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (!objs.isEmpty()) {
            String table = getMapping().getTable();
            List<ColumnMapping> relevantColumnMappings = new ArrayList<>();
            for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
                if (columnMapping.isInsertable()) {
                    relevantColumnMappings.add(columnMapping);
                }
            }
            List<String> columns = new ArrayList<>();
            for (ColumnMapping columnMapping : relevantColumnMappings) {
                columns.add(columnMapping.getColumn());
            }
            List<List<Object>> values = new ArrayList<>();
            for (T obj : objs) {
                List<Object> vals = new ArrayList<>();
                for (ColumnMapping columnMapping : relevantColumnMappings) {
                    vals.add(columnMapping.readProperty(obj));
                }
                values.add(vals);
            }
            List<Object> ids = getQueryTarget().insert(table)
                    .columns(columns)
                    .addMany(values)
                    .execute();
            for (int i = 0; i < objs.size(); i++) {
                getMapping().getIdColumnMapping().writeProperty(objs.get(i), ids.get(i));
            }
        }
    }

}
