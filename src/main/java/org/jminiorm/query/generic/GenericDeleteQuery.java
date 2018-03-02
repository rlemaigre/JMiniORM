package org.jminiorm.query.generic;

import java.util.ArrayList;
import java.util.List;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;

public class GenericDeleteQuery extends AbstractGenericQuery implements IGenericDeleteQuery {

    private String table;
    private String idColumn;
    private List<Object> ids = new ArrayList<>();

    public GenericDeleteQuery(IQueryTarget target) {
        super(target);
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
    public void execute() throws DBException {
        if (ids.isEmpty()) return;

        // SQL :
        String sql = getQueryTarget().getConfig().getDialect().sqlForDelete(table, idColumn);

        // Parameters :
        List<List<Object>> params = new ArrayList<>();
        for (Object id : ids) {
            List<Object> curParams = new ArrayList<>();
            curParams.add(id);
            params.add(curParams);
        }

        getQueryTarget().executeUpdate(sql, params, null);
    }
}
