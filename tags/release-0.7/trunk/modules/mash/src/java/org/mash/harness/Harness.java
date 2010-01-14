package org.mash.harness;

import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;

import java.util.List;

/**
 * A harness is a plugin for a component of your framework.
 * <p/>
 * The thought behind this framework is that to properly system test you need to act as an endpoint (user, email client)
 * as far out from the main system as possible.  For example, a web harness should act as a web browser, and
 * verification happens on the web result data.
 * <p/>
 * With some harnesses, this isn't possible.  For example, a db setup harness needs to insert data directly into the db
 * and therefore has access to a central point in the system.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 10:02:30 AM
 */
public interface Harness
{
    /**
     * Apply the configurations from the test files to the harness.  These are implementation specific and should be
     * documented appropriately.
     * <p/>
     * Each implementation will have its own configuration needs and should be documented appropriately.  For example, a
     * web harness would need a url to connect to, or a db harness would need a set of sql to run.
     *
     * @param configs for the harness implementation
     */
    void setConfiguration(List<Configuration> configs);

    /**
     * Apply the parameters from the test files to the harness implementation.  These are implementation specific and
     * should be documented appropriately.
     * <p/>
     * Parameters have more flexibility than configurations, as the harness should use parameters to pass onto the
     * component that the harness invokes.  For example, a web harness may take these parameters and post them as form
     * data.
     *
     * @param params for the harness implementation
     */
    void setParameters(List<Parameter> params);

    /**
     * Supply the test harness definitions for use by the defined harness
     * @return original definition of the harness
     */
    HarnessDefinition getDefinition();

    /**
     * Processes need to have this definition, so you must store it if implementing a harness
     * @param harnessDefinition for retrieval later
     */
    void setDefinition(HarnessDefinition harnessDefinition);

    /**
     * Each test has a set of potential errors.  These errors are used by the test runner (e.g. junit) to determine how
     * the test should continue (fail, ignore, etc)
     *
     * @return list of errors
     */
    List<HarnessError> getErrors();

    List<Configuration> getConfiguration();

    List<Parameter> getParameters();
}
