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
    public IORMQuery<T> forClass(Class<T> clazz) {
        this.clazz = clazz;
        return this;
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
        return getQueryTarget().getConfig().getORMappingProvider().getORMapping(getTargetClass());
    }

    protected void verifySchemaExistence() {
        String schema = getMapping().getSchema();
        if (schema != null && !schema.isEmpty()) {
            String sql = getQueryTarget().getConfig().getDialect().sqlForCreateSchema(schema);
            if (sql != null) getQueryTarget().sql(sql);
        }
    }
}
