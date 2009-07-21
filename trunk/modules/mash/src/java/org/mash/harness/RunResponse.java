package org.mash.harness;

import java.util.Collection;

/**
 * Responses for each run should provide these accessors for 'generic' verification utilities.  More specific
 * verification utilities may have to cast to the run harness implementation to retrieve some response value, however
 * this should be avoided.
 * <p/>
 * User: teastlack Date: Jul 1, 2009 Time: 9:23:44 AM
 */
public interface RunResponse {
    /**
     * Retrieve a value using the supplied name.
     * <p/>
     * This obviously changes depending on implementation.  For example, a web response would have a name / value
     * pairing, whereas an XML response would have an xpath / value pairing.
     *
     * @param name to search the response for
     * @return result of the response.  Could be null if not present.
     */
    String getValue(String name);

    /**
     * Retrieve all values with the supplied name
     * <p/>
     * This obviously changes depending on implementation.  For example, a web response would have a name / value
     * pairing, whereas an XML response would have an xpath / value pairing.
     *
     * @param name to search the response for
     * @return list of all values with this name
     */
    Collection<String> getValues(String name);

    /**
     * Return the response as a string. For a web response, this would probably be the HTML, for an XML response, the
     * XML.
     *
     * @return response
     */
    String getString();
}
