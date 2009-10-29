package org.mash.harness.db;

/**
 * DBWorkers allow various custom means of connecting to the database with flat dbunit XML or straightup sql.
 * <p/>
 * User: teastlack Date: Jul 7, 2009 Time: 6:33:35 PM
 */
public interface DBWorker
{
    /**
     * Execute the specified SQL against the database described by the connector.
     *
     * @param connector information to connect to the db
     * @param sql       to run
     * @throws Exception when sql fails
     */
    public void execute(DBConnector connector, String sql) throws Exception;

    /**
     * Perform an update using the flat file xml dataset used by DBUnit
     *
     * @param connector information to connect to the db
     * @param operation the operation to perform
     * @param content   the xml data set to load
     * @throws Exception when load fails
     */
    public void updateRows(DBConnector connector, String operation, String content) throws Exception;
}
