package org.mash.harness.rest;

import org.mash.harness.http.Method;

/**
 * @author:
 * @since: Aug 1, 2009
 */
public enum Verb
{
    CREATE(Method.PUT),
    UPDATE(Method.POST),
    READ(Method.GET),
    DELETE(Method.DELETE);

    private Method method;

    Verb(Method method)
    {
        this.method = method;
    }

    public Method getMethod()
    {
        return method;
    }
}
