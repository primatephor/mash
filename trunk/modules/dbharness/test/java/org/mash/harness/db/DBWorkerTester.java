package org.mash.harness.db;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 5:03:02 PM
 *
 */
public class DBWorkerTester implements DBWorker
{
    public boolean executeCalled = false;
    public boolean updateCalled = false;
    private DBConnector connector;
    private String operation;
    private String content;

    public void execute(DBConnector connector, String sql) throws Exception
    {
        executeCalled = true;
        this.connector = connector;
        this.content = sql;
    }

    public void updateRows(DBConnector connector, String operation, String content) throws Exception
    {
        updateCalled = true;
        this.connector = connector;
        this.content = content;
        this.operation = operation;
    }

    public DBConnector getConnector()
    {
        return connector;
    }

    public String getOperation()
    {
        return operation;
    }

    public String getContent()
    {
        return content;
    }
}
