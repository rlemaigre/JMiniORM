package org.jminiorm.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging statement executor that uses the SLF4J framework.
 */
public class SLF4JLoggingStatementExecutor extends AbstractLoggingStatementExecutor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoggingStatementExecutor.class);

    public SLF4JLoggingStatementExecutor(IStatementExecutor wrapped) {
        super(wrapped);
    }

    @Override
    protected void log(String message) {
        logger.info(message);
    }

}
