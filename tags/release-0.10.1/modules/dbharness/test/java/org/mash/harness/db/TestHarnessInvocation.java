package org.mash.harness.db;

import junit.framework.TestCase;
import org.mash.config.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 5:00:53 PM
 *
 */
public class TestHarnessInvocation extends TestCase
{
    public void testCallXML() throws Exception
    {
        DBSetupHarness harness = new MySetupHarness();
        harness.setType("DELETE");
        harness.setUrl("bogus.url");
        harness.setUser("bogus.user");
        harness.setPassword("bogus.pass");
        harness.setDriver("bogus.driver");
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("loadfile", null, "org/mash/harness/db/sample_clean.xml"));
        harness.setParameters(params);
        harness.setup();

        DBUnitWorkerTester worker = (DBUnitWorkerTester) harness.getDBUnitWorker();
        assertEquals(true, worker.updateCalled);
        assertEquals(false, worker.executeCalled);
        assertEquals("DELETE", worker.getOperation());
    }

    public void testCallSQL() throws Exception
    {
        DBSetupHarness harness = new MySetupHarness();
        harness.setType("DELETE");
        harness.setUrl("bogus.url");
        harness.setUser("bogus.user");
        harness.setPassword("bogus.pass");
        harness.setDriver("bogus.driver");
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("loadfile", null, "org/mash/harness/db/sample_clean.sql"));
        harness.setParameters(params);
        harness.setup();

        JDBCWorkerTester worker = (JDBCWorkerTester) harness.getJDBCWorker();
        assertEquals(false, worker.updateCalled);
        assertEquals(true, worker.executeCalled);
    }

    private class MySetupHarness extends DBSetupHarness
    {
        private DBUnitWorker dbunit = new DBUnitWorkerTester();
        private JDBCWorkerTester jdbc = new JDBCWorkerTester();

        public DBUnitWorker getDBUnitWorker()
        {
            return dbunit;
        }

        public JDBCWorker getJDBCWorker()
        {
            return jdbc;
        }
    }
}
