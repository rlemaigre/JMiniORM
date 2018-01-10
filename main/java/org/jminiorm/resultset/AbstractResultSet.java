package org.jminiorm.resultset;

import org.jminiorm.exception.DBException;
import org.jminiorm.exception.UnexpectedNumberOfItemsException;
import org.jminiorm.query.IQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractResultSet<T> implements IResultSet<T> {

    private IQuery query;
    private List<Map<String, Object>> rawResultSet;

    public AbstractResultSet(IQuery query, List<Map<String, Object>> rawResultSet) {
        this.query = query;
        this.rawResultSet = rawResultSet;
    }

    @Override
    public T one() throws UnexpectedNumberOfItemsException, DBException {
        List<T> rs = list();
        if (rs.size() != 1) throw new UnexpectedNumberOfItemsException(rs.size());
        else return rs.get(0);
    }

    @Override
    public T first() throws DBException {
        List<T> rs = list();
        if (rs.isEmpty()) return null;
        else return rs.get(0);
    }

    @Override
    public List<T> list() throws DBException {
        return rawResultSet.stream().map(this::castRow).collect(Collectors.toList());
    }

    /**
     * Returns the query that generated this result set.
     *
     * @return
     */
    protected IQuery getQuery() {
        return query;
    }

    /**
     * Cast the row into type T.
     *
     * @param row
     * @return
     */
    protected abstract T castRow(Map<String, Object> row);
}
