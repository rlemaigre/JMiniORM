package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMDeleteQuery<T> extends AbstractQuery implements IORMDeleteQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMDeleteQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public IORMDeleteQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public IORMDeleteQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() {
        // TODO
    }
}
