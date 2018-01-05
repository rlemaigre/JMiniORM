package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.List;
import java.util.Map;

public class GenericUpdateQuery extends AbstractQuery implements IGenericUpdateQuery {

    private String table;
    private String idColumn;
    private List<Map<String,Object>> values;

    public GenericUpdateQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericUpdateQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public GenericUpdateQuery idColumn(String idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    @Override
    public IGenericUpdateQuery addOne(Map<String, Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IGenericUpdateQuery addMany(List<Map<String, Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() throws DBException {
        // TODO
    }
}
