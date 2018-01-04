package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuery extends AbstractQuery implements IUpdateQuery {

    private String table;
    private String idColumn;
    private List<String> columns;
    private List<Object> ids = new ArrayList<>();
    private List<List<Object>> values = new ArrayList<>();

    public UpdateQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IUpdateQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public UpdateQuery idColumn(String idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    @Override
    public UpdateQuery columns(List<String> cols) {
        columns = cols;
        return this;
    }

    @Override
    public UpdateQuery addOne(Object id, List<Object> values) {
        ids.add(id);
        this.values.add(values);
        return this;
    }

    @Override
    public UpdateQuery addMany(List<Object> ids, List<List<Object>> values) {
        this.ids.addAll(ids);
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() {
        // TODO
    }
}
