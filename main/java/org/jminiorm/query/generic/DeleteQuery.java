package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.List;

public class DeleteQuery extends AbstractQuery implements IDeleteQuery {

    private String table;
    private String idColumn;
    private List<Object> ids = new ArrayList<>();

    public DeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IDeleteQuery table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public IDeleteQuery idColumn(String idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    @Override
    public IDeleteQuery addOne(Object id) {
        this.ids.add(id);
        return this;
    }

    @Override
    public IDeleteQuery addMany(List<Object> ids) {
        this.ids.addAll(ids);
        return this;
    }

    @Override
    public void execute() {
        // TODO
    }
}
