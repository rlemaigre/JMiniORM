package org.jminiorm.query;

import org.jminiorm.IQueryTarget;

public class AbstractQuery implements IQuery {

    private IQueryTarget target;

    public AbstractQuery(IQueryTarget target) {
        setQueryTarget(target);
    }

    @Override
    public void setQueryTarget(IQueryTarget target) {
        this.target = target;
    }

    @Override
    public IQueryTarget getQueryTarget() {
        return target;
    }

}
