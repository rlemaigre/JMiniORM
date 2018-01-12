package org.jminiorm;

import java.util.Objects;

public class SubBean {

    private int x;

    public SubBean() {
    }

    public SubBean(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubBean subBean = (SubBean) o;
        return x == subBean.x;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x);
    }
}
