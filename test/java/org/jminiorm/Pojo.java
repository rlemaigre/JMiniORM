package org.jminiorm;

import javax.persistence.Column;

public class Pojo {

    @Column(name = "column_name")
    private String field;
    private String type;

    public Pojo() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
