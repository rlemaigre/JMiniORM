package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericInsertQuery extends AbstractQuery implements IGenericInsertQuery {

    private String table;
    private String generatedColumn;
    private List<Map<String, Object>> values = new ArrayList<>();

    public GenericInsertQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericInsertQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IGenericInsertQuery generatedColumn(String column) {
        generatedColumn = column;
        return this;
    }

    @Override
    public IGenericInsertQuery addOne(Map<String, Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IGenericInsertQuery addMany(List<Map<String, Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (values.isEmpty()) return;

        // Columns :
        List<String> columns = new ArrayList<>(values.get(0).keySet());

        // SQL :
        String sql = getQueryTarget().getDialect().sqlForInsert(table, columns);

        // Parameters :
        List<List<Object>> params = new ArrayList<>();
        for (Map<String, Object> val : values) {
            List<Object> curParams = new ArrayList<>();
            for (String col : columns) {
                curParams.add(val.get(col));
            }
            params.add(curParams);
        }

        // Execute query :
        getQueryTarget().executeUpdate(sql, params);
    }
}
