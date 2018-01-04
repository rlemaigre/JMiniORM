package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertQuery extends AbstractQuery implements IInsertQuery {

    private String table;
    private String generatedColumn;
    private List<Map<String, Object>> values = new ArrayList<>();

    public InsertQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IInsertQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IInsertQuery generatedColumn(String column) {
        this.generatedColumn = column;
        return this;
    }

    @Override
    public IInsertQuery addOne(Map<String, Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IInsertQuery addMany(List<Map<String, Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }
}
