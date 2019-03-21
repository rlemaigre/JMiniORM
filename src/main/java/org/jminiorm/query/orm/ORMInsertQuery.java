package org.jminiorm.query.orm;

import org.jminiorm.IQueryTarget;
import org.jminiorm.exception.DBException;
import org.jminiorm.mapping.ColumnMapping;

import java.util.*;

public class ORMInsertQuery<T> extends AbstractORMQuery<T> implements IORMInsertQuery<T> {

	private List<T> objs = new ArrayList<>();

	public ORMInsertQuery(IQueryTarget target) {
		super(target);
	}

	@Override
	public IORMInsertQuery<T> forClass(Class<T> clazz) {
		return (IORMInsertQuery<T>) super.forClass(clazz);
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
	public void execute() throws DBException {
		if (!objs.isEmpty()) {
			// The table to insert the rows into :
			String table = getMapping().getTable();

			// The column mappings for all insertable columns that are not generated :
			List<ColumnMapping> relevantColumnMappings = new ArrayList<>();
			for (ColumnMapping columnMapping : getMapping().getColumnMappings()) {
				if (columnMapping.isInsertable() && !columnMapping.isGenerated()) {
					relevantColumnMappings.add(columnMapping);
				}
			}

			// The maps with the column => value pairs to insert :
			List<Map<String, Object>> rows = new ArrayList<>();
			for (T obj : objs) {
				Map<String, Object> row = new HashMap<>();
				for (ColumnMapping columnMapping : relevantColumnMappings) {
					row.put(columnMapping.getColumn(), columnMapping.readProperty(obj));
				}
				rows.add(row);
			}

			// Insert rows :
			ColumnMapping idColumnMapping = getMapping().hasId() ? getMapping().getIdColumnMapping() : null;
			getQueryTarget().insert(table).schema(getMapping().getSchema())
					.generatedColumn(
							idColumnMapping != null && idColumnMapping.isGenerated() ? idColumnMapping.getColumn()
									: null)
					.addMany(rows)
					.execute();

			// Assign generated ids if any :
			if (idColumnMapping != null && idColumnMapping.isGenerated()) {
				for (int i = 0; i < objs.size(); i++) {
					Long key = (Long) rows.get(i).get(idColumnMapping.getColumn());
					if (idColumnMapping.getPropertyDescriptor().getPropertyType() == Long.class)
						idColumnMapping.writeProperty(objs.get(i), key);
					else
						idColumnMapping.writeProperty(objs.get(i), key.intValue());
				}
			}
		}
	}

}
