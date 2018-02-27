package org.jminiorm.exception;

/**
 * All SQL exceptions that occur in the ORM are wrapped in this unchecked exception class, because most of the time
 * there is nothing the caller can do about it. This avoid cluttering client code with try and catch blocks each time
 * the database is accessed.
 */
@SuppressWarnings("serial")
public class DBException extends RuntimeException {

    public DBException(Throwable t) {
        super(t);
    }

    public DBException(String string) {
        this(new Exception(string));
    }

}
