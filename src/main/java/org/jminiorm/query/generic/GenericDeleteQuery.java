package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericDeleteQuery extends AbstractGenericQuery implements IGenericDeleteQuery {

    private String schema;
    private String table;
    private String idColumn;
    private List<Object> ids = new ArrayList<>();
    private String where;
    private List<Object> params;

    public GenericDeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IGenericDeleteQuery schema(String schema) {
        this.schema = schema;
        return this;
    }

    @Override
    public IGenericDeleteQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IGenericDeleteQuery idColumn(String idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    @Override
    public IGenericDeleteQuery addOne(Object id) {
        ids.add(id);
        return this;
    }

    @Override
    public IGenericDeleteQuery addMany(List<Object> ids) {
        this.ids.addAll(ids);
        return this;
    }

    @Override
    public IGenericDeleteQuery where(String where, Object... params) {
        this.where = where;
        this.params = Arrays.asList(params);
        return this;
    }

    @Override
    public void execute() throws DBException {
        if (!ids.isEmpty()) {
            // SQL :
            String sql = getQueryTarget().getConfig().getDialect().sqlForDelete(schema, table, idColumn);

            // Parameters :
            List<List<Object>> params = new ArrayList<>();
            for (Object id : ids) {
                List<Object> curParams = new ArrayList<>();
                curParams.add(id);
                params.add(curParams);
            }

            getQueryTarget().executeUpdate(sql, params, null);
        }
        if (where != null) {
            String sql = getQueryTarget().getConfig().getDialect().sqlForDeleteWhere(schema, table, where);
            getQueryTarget().executeUpdate(sql, Arrays.asList(params), null);
        }
    }
}
