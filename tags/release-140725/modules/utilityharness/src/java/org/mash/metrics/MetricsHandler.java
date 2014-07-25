package org.mash.metrics;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author teastlack
 * @since Feb 22, 2011 9:03:08 AM
 */
public class MetricsHandler implements InvocationHandler
{
    private static final Logger log = Logger.getLogger(MetricsHandler.class.getName());
    Object target;

    public MetricsHandler(Object target)
    {
        this.target = target;
    }

    //just in case it's not wired

    public static Object newInstance(Object obj, Class... interfaces)
    {
        if (interfaces != null && interfaces.length > 0)
        {
            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), interfaces, new MetricsHandler(obj));
        }
        else
        {
            log.warn("Unable to create proxy, no interfaces specified");
            return obj;
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object result = null;
        Metrics stats = null;
        if (target != null)
        {
            stats = MetricsManager.start(target.getClass(), method);
        }

        try
        {
            result = method.invoke(target, args);
        }
        catch (InvocationTargetException e)
        {
            //remove the proxy bs from the stack trace
            if (e.getCause() != null)
            {
                throw e.getCause();
            }
            else
            {
                log.error("Problem invoking " + target.getClass() + "." + method.getName(), e);
            }
        }
        finally
        {
            if (stats != null)
            {
                stats.end();
            }
        }
        return result;
    }
}
