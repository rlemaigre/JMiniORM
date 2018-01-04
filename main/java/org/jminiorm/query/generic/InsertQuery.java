package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;

public class InsertQuery extends AbstractQuery implements IInsertQuery {

    private String table;
    private List<String> columns = new ArrayList<>();
    private List<List<Object>> values = new ArrayList<>();

    public InsertQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IInsertQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IInsertQuery columns(List<String> cols) {
        columns = cols;
        return this;
    }

    @Override
    public IInsertQuery addOne(List<Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IInsertQuery addMany(List<List<Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public List<Object> execute() {
        // TODO
    }
}
