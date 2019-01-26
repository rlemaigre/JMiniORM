package org.jminiorm;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.generic.*;
import org.jminiorm.query.orm.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractQueryTarget implements IQueryTarget {

    @Override
    public <T> IORMSelectQuery<T> select(Class<T> clazz) {
        return new ORMSelectQuery<T>(this).forClass(clazz);
    }

    @Override
    public <T> T select(Class<T> clazz, Object id) throws DBException {
        return select(clazz).id(id).first();
    }

    @Override
    public IGenericSelectQuery select(String sql, Object... params) {
        return new GenericSelectQuery(this).sql(sql, params);
    }

    @Override
    public <T> IORMInsertQuery<T> insert(Class<T> clazz) {
        return new ORMInsertQuery<T>(this).forClass(clazz);
    }

    @Override
    public <T> void insert(T obj) throws DBException {
        insert((Class<T>) obj.getClass()).addOne(obj).execute();
    }

    @Override
    public <T> void insert(Collection<T> objs) throws DBException {
        if (objs.isEmpty()) return;
        insert((Class<T>) objs.iterator().next().getClass()).addMany(objs).execute();
    }

    @Override
    public IGenericInsertQuery insert(String table) throws DBException {
        return new GenericInsertQuery(this).table(table);
    }

    @Override
    public <T> IORMUpdateQuery<T> update(Class<T> clazz) {
        return new ORMUpdateQuery<T>(this).forClass(clazz);
    }

    @Override
    public <T> void update(T obj) throws DBException {
        update((Class<T>) obj.getClass()).addOne(obj).execute();
    }

    @Override
    public <T> void update(Collection<T> objs) throws DBException {
        if (objs.isEmpty()) return;
        update((Class<T>) objs.iterator().next().getClass()).addMany(objs).execute();
    }

    @Override
    public IGenericUpdateQuery update(String table) throws DBException {
        return new GenericUpdateQuery(this).table(table);
    }

    @Override
    public <T> IORMDeleteQuery<T> delete(Class<T> clazz) {
        return new ORMDeleteQuery<T>(this).forClass(clazz);
    }

    @Override
    public <T> void delete(Class<T> clazz, Object id) throws DBException {
        delete(clazz).id(id).execute();
    }

    @Override
    public <T> void delete(T obj) throws DBException {
        delete((Class<T>) obj.getClass()).addOne(obj).execute();
    }

    @Override
    public <T> void delete(Collection<T> objs) throws DBException {
        if (objs.isEmpty()) return;
        delete((Class<T>) objs.iterator().next().getClass()).addMany(objs).execute();
    }

    @Override
    public IGenericDeleteQuery delete(String table) throws DBException {
        return new GenericDeleteQuery(this).table(table);
    }

    @Override
    public <T> void createTable(Class<T> clazz) throws DBException {
        new ORMCreateTableQuery<T>(this).forClass(clazz).execute();
    }

    @Override
    public <T> void dropTable(Class<T> clazz) throws DBException {
        new ORMDropTableQuery<T>(this).forClass(clazz).execute();
    }

    @Override
    public void sql(String sql, Object... params) throws DBException {
        new GenericRawQuery(this).sql(sql, params).execute();
    }

    @Override
    public List<Long> executeUpdate(String sql, List<List<Object>> params, String generatedColumn) throws DBException {
        return getConfig().getStatementExecutor().executeUpdate(this, sql, params, generatedColumn);
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, List<Object> params,
                                                  Map<String, Class<?>> typeOverrides) throws DBException {
        return getConfig().getStatementExecutor().executeQuery(this, sql, params, typeOverrides);
    }
}
