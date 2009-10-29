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
        System.setProperty("db.setup.class", "org.mash.harness.db.DBWorkerTester");
        DBSetupHarness harness = new DBSetupHarness();
        harness.setType("DELETE");
        harness.setUrl("bogus.url");
        harness.setUser("bogus.user");
        harness.setPassword("bogus.pass");
        harness.setDriver("bogus.driver");
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("loadfile", null, "org/mash/harness/db/sample_clean.xml"));
        harness.setParameters(params);
        harness.setup();

        DBWorkerTester worker = (DBWorkerTester) harness.getWorker();
        assertEquals(true, worker.updateCalled);
        assertEquals(false, worker.executeCalled);
        assertEquals("DELETE", worker.getOperation());
    }

    public void testCallSQL() throws Exception
    {
        System.setProperty("db.setup.class", "org.mash.harness.db.DBWorkerTester");
        DBSetupHarness harness = new DBSetupHarness();
        harness.setType("DELETE");
        harness.setUrl("bogus.url");
        harness.setUser("bogus.user");
        harness.setPassword("bogus.pass");
        harness.setDriver("bogus.driver");
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("loadfile", null, "org/mash/harness/db/sample_clean.sql"));
        harness.setParameters(params);
        harness.setup();

        DBWorkerTester worker = (DBWorkerTester) harness.getWorker();
        assertEquals(false, worker.updateCalled);
        assertEquals(true, worker.executeCalled);
    }
}
