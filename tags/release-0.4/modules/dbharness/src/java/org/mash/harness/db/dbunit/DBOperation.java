package org.mash.harness.db.dbunit;

import org.dbunit.operation.DatabaseOperation;

/**
 * User: teastlack Date: Jul 7, 2009 Time: 6:09:15 PM
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

    private DatabaseOperation operation;

    DBOperation(DatabaseOperation operation)
    {
        this.operation = operation;
    }

    public DatabaseOperation getOperation()
    {
        return operation;
    }
}
