package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateQuery extends AbstractQuery implements IUpdateQuery {

    private String table;
    private String idColumn;
    private List<Map<String,Object>> values;

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
    public IUpdateQuery addOne(Map<String, Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IUpdateQuery addMany(List<Map<String, Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }
}
