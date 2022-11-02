package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.query.AbstractQuery;

import java.util.*;

public class GenericUpdateQuery extends AbstractQuery implements IGenericUpdateQuery {

    private String schema;
    private String table;
    private List<String> idColumns;
    private List<Map<String,Object>> values = new ArrayList<>();

    public GenericUpdateQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericUpdateQuery schema(String schema) {
        this.schema = schema;
        return this;
    }

    @Override
    public IGenericUpdateQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IGenericUpdateQuery idColumn(String idColumn) {
        this.idColumns = Collections.singletonList(idColumn);
        return this;
    }

    @Override
    public IGenericUpdateQuery idColumns(String... idColumns) {
        this.idColumns = Arrays.asList(idColumns);
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
        columns.removeAll(idColumns);

        // SQL :
        String sql = getQueryTarget().getConfig().getDialect().sqlForUpdate(schema, table, idColumns, columns);

        // Parameters :
        List<List<Object>> params = new ArrayList<>();
        for (Map<String,Object> val : values) {
            List<Object> curParams = new ArrayList<>();
            for (String col : columns) {
                curParams.add(val.get(col));
            }
            for (String col : idColumns) {
                curParams.add(val.get(col));
            }
            params.add(curParams);
        }

        // Execute query :
        getQueryTarget().executeUpdate(sql, params, null);
    }

}
