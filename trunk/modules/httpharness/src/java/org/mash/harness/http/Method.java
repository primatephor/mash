package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.HttpMethod;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public enum Method
{
    POST(HttpMethod.POST),
    DELETE(HttpMethod.DELETE),
    PUT(HttpMethod.PUT),
    GET(HttpMethod.GET);

    private HttpMethod method;

    Method(HttpMethod method)
    {
        this.method = method;
    }

    public HttpMethod getMethod()
    {
        return method;
    }
}
