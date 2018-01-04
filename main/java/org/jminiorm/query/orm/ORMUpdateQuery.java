package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ORMUpdateQuery<T> extends AbstractQuery implements IORMUpdateQuery<T> {

    private List<T> objs = new ArrayList<>();

    public ORMUpdateQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public ORMUpdateQuery<T> addOne(T obj) {
        objs.add(obj);
        return this;
    }

    @Override
    public ORMUpdateQuery<T> addMany(Collection<T> objs) {
        this.objs.addAll(objs);
        return this;
    }

    @Override
    public void execute() {
        // TODO
    }

}
