package org.mash.harness.db;

/**
 * @author teastlack
 * @since Jun 9, 2010 6:02:57 PM
 */
public class JDBCWorkerTester extends JDBCWorker
{
    public boolean executeCalled = false;
    public boolean updateCalled = false;
    private DBConnector connector;
    private String content;

    public void execute(DBConnector connector, String sql) throws Exception
    {
        executeCalled = true;
        this.connector = connector;
        this.content = sql;
    }

    public DBConnector getConnector()
    {
        return connector;
    }

    public String getContent()
    {
        return content;
    }
}
