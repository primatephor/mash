package org.mash.harness.db.dbunit;

import org.dbunit.operation.DatabaseOperation;
import org.apache.log4j.Logger;

/**
 *  Date: Jul 7, 2009 Time: 6:09:15 PM
 */
public enum DBOperation
{
    CLEAN_INSERT(DatabaseOperation.CLEAN_INSERT),
    DELETE(DatabaseOperation.DELETE),
    DELETE_ALL(DatabaseOperation.DELETE_ALL),
    INSERT(DatabaseOperation.INSERT),
    REFRESH(DatabaseOperation.REFRESH),
    TRUNCATE(DatabaseOperation.TRUNCATE_TABLE),
    UPDATE(DatabaseOperation.UPDATE);

    private static final Logger LOG = Logger.getLogger(DBOperation.class.getName());
    private DatabaseOperation operation;

    DBOperation(DatabaseOperation operation)
    {
        this.operation = operation;
    }

    public DatabaseOperation getOperation()
    {
        return operation;
    }

    public static DBOperation find(String operationName)
    {
        LOG.debug("Looking for operation '" + operationName + "'");
        DBOperation result = null;
        if (operationName != null &&
            operationName.length() > 0)
        {
            try
            {
                result = DBOperation.valueOf(operationName.toUpperCase());
                LOG.debug("Found " + result.name());
            }
            catch (IllegalArgumentException e)
            {
                LOG.error("Problem setting db operation", e);
            }
        }
        return result;
    }
}
