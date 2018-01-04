package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.mapping.ORMapping;
import org.jminiorm.query.AbstractQuery;

public abstract class AbstractORMQuery<T> extends AbstractQuery implements IORMQuery<T> {

    private Class<T> clazz;

    public AbstractORMQuery(IQueryTarget target) {
        super(target);
    }

    @Override
    public void forClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Class<T> getTargetClass() {
        return clazz;
    }

    /**
     * Returns the object-relational mapping for the jpa-annotated class this query is about.
     *
     * @return
     */
    protected ORMapping getMapping() {
        return getQueryTarget().getORMappingProvider().getORMapping(getTargetClass());
    }

}
