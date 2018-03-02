package org.jminiorm.query.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

public class GenericUpdateQuery extends AbstractQuery implements IGenericUpdateQuery {

    private String table;
    private String idColumn;
    private List<Map<String,Object>> values = new ArrayList<>();

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
    public IGenericUpdateQuery addOne(Map<String,Object> values) {
        this.values.add(values);
        return this;
    }

    @Override
    public IGenericUpdateQuery addMany(List<Map<String,Object>> values) {
        this.values.addAll(values);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (values.isEmpty()) return;

        // Columns (x in "SET x = ...") :
        List<String> columns = new ArrayList<>(values.get(0).keySet());
        columns.remove(idColumn);

        // SQL :
        String sql = getQueryTarget().getConfig().getDialect().sqlForUpdate(table, idColumn, columns);

        // Parameters :
        List<List<Object>> params = new ArrayList<>();
        for (Map<String,Object> val : values) {
            List<Object> curParams = new ArrayList<>();
            for (String col : columns) {
                curParams.add(val.get(col));
            }
            curParams.add(val.get(idColumn));
            params.add(curParams);
        }

        // Execute query :
        getQueryTarget().executeUpdate(sql, params, null);
    }

}
