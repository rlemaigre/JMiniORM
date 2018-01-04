package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMInsertQuery<T> extends AbstractQuery implements IORMInsertQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMInsertQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public ORMInsertQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public ORMInsertQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() {
        // TODO
    }

}
