package org.mash.harness.http;

import com.meterware.httpunit.WebRequest;

import java.util.Map;

/**
 * @author: teastlack
 * @since: Jul 26, 2009
 */
public interface WebRequestFactory
{
    WebRequest createRequest(String methodType,
                                    String url,
                                    Map<String, String> contents);
}
