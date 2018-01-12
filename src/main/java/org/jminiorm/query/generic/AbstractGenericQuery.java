package org.jminiorm.query.generic;

import org.jminiorm.IQueryTarget;
import org.jminiorm.query.AbstractQuery;

public abstract class AbstractGenericQuery extends AbstractQuery implements IGenericQuery {

    public AbstractGenericQuery(IQueryTarget target) {
        super(target);
    }

}
