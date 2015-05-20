package org.mash.metrics;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author
 * @since Feb 22, 2011 8:57:22 AM
 */
public class MetricsInterceptor implements org.aopalliance.intercept.MethodInterceptor
{
    public Object invoke(MethodInvocation methodInvocation) throws Throwable
    {
        Object result = null;
        Method method = methodInvocation.getMethod();
        Class declaring = method.getDeclaringClass();
        Metrics stats = MetricsManager.start(declaring, method);
        try
        {
            result = methodInvocation.proceed();
        }
        finally
        {
            stats.end();
        }
        return result;
    }
}
