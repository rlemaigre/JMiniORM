package org.jminiorm.exception;

/**
 * All SQL exceptions that may occur in the ORM are wrapped in this unchecked exception class, because there is nothing
 * the caller can do about it.
 */
public class DBException extends RuntimeException {

    public DBException(Throwable t) {
        super(t);
    }

}
