package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.WebRequest;

import java.util.Map;

/**
 * @author:
 * @since: Jul 26, 2009
 */
public interface WebRequestFactory
{
    WebRequest createRequest(String methodType,
                             String url,
                             Map<String, String> contents) throws Exception;
}
