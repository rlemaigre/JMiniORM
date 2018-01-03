package org.jminiorm.mapping;

/**
 * Represents an index to be added to a table.
 */
public class Index {

    /**
     * The name of the index.
     */
    private String name;

    /**
     * The columns of the index, for example, "x ASC, b DESC".
     */
    private String columns;

    /**
     * Whether this index is unique or not.
     */
    private boolean unique;

    public Index() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
}
