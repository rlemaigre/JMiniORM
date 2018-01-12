package org.jminiorm.exception;

/**
 * The ORM throws this exception with exactly one element is expected to be returned by a select query, but zero or more
 * than one are encountered instead.
 */
public class UnexpectedNumberOfItemsException extends Exception {

    private int actual;

    public UnexpectedNumberOfItemsException(int actual) {
        this.actual = actual;
    }

    /**
     * Returns the number of elements encountered instead where exactly one was expected.
     *
     * @return the number of elements encountered instead where exactly one was expected.
     */
    public int getActual() {
        return actual;
    }
}
