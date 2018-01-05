package org.jminiorm;

import org.jminiorm.exception.DBException;
import org.jminiorm.query.generic.IDeleteQuery;
import org.jminiorm.query.generic.IInsertQuery;
import org.jminiorm.query.generic.ISelectQuery;
import org.jminiorm.query.generic.IUpdateQuery;
import org.jminiorm.query.orm.IORMSelectQuery;
import org.jminiorm.query.orm.ORMCreateTableQuery;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

public abstract class AbstractQueryTarget implements IQueryTarget {

    @Override
    public <T> T select(Class<T> clazz, Object id) throws DBException {
        return null;
    }

    @Override
    public <T> IORMSelectQuery<T> select(Class<T> clazz) {
        return null;
    }

    @Override
    public ISelectQuery select(String sql, Object... params) {
        return null;
    }

    @Override
    public <T> void insert(T obj) throws DBException {

    }

    @Override
    public <T> void insert(Collection<T> objs) throws DBException {

    }

    @Override
    public IInsertQuery insert(String table) throws DBException {
        return null;
    }

    @Override
    public <T> void update(T obj) throws DBException {

    }

    @Override
    public <T> void update(Collection<T> objs) throws DBException {

    }

    @Override
    public IUpdateQuery update(String table) throws DBException {
        return null;
    }

    @Override
    public <T> void delete(Class<T> clazz, Object id) throws DBException {

    }

    @Override
    public <T> void delete(T obj) throws DBException {
    }

    @Override
    public <T> void delete(Collection<T> objs) throws DBException {

    }

    @Override
    public IDeleteQuery delete(String table) throws DBException {
        return null;
    }

    @Override
    public <T> void createTable(Class<T> clazz) throws DBException {
        new ORMCreateTableQuery<T>(this).forClass(clazz).execute();
    }

    @Override
    public void sql(String sql, Object... params) throws DBException {

    }

    @Override
    public List<Object> executeUpdate(String sql, List<List<Object>> params) throws DBException {
        return getStatementExecutor().executeUpdate(this, sql, params);
    }

    @Override
    public ResultSet executeQuery(String sql, List<Object> params) throws DBException {
        return null;
    }
}