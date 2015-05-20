package org.mash.harness.message.jms;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.Binding;
import javax.naming.NameParser;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author
 * @since Jan 29, 2010 3:45:56 PM
 *
 */
public class ConfigInitialContext implements Context
{
    private Map<String, Object> data = new HashMap<String, Object>();

    public ConfigInitialContext() throws NamingException
    {
    }

    public Object lookup(String name) throws NamingException
    {
        return data.get(name);
    }

    public void addData(String name, Object value)
    {
        data.put(name, value);
    }

    public Object lookup(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method lookup not yet implemented");
    }

    public void bind(Name name, Object obj) throws NamingException
    {
        throw new UnsupportedOperationException("Method bind not yet implemented");
    }

    public void bind(String name, Object obj) throws NamingException
    {
        throw new UnsupportedOperationException("Method bind not yet implemented");
    }

    public void rebind(Name name, Object obj) throws NamingException
    {
        throw new UnsupportedOperationException("Method rebind not yet implemented");
    }

    public void rebind(String name, Object obj) throws NamingException
    {
        throw new UnsupportedOperationException("Method rebind not yet implemented");
    }

    public void unbind(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method unbind not yet implemented");
    }

    public void unbind(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method unbind not yet implemented");
    }

    public void rename(Name oldName, Name newName) throws NamingException
    {
        throw new UnsupportedOperationException("Method rename not yet implemented");
    }

    public void rename(String oldName, String newName) throws NamingException
    {
        throw new UnsupportedOperationException("Method rename not yet implemented");
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method list not yet implemented");
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method list not yet implemented");
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method listBindings not yet implemented");
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method listBindings not yet implemented");
    }

    public void destroySubcontext(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method destroySubcontext not yet implemented");
    }

    public void destroySubcontext(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method destroySubcontext not yet implemented");
    }

    public Context createSubcontext(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method createSubcontext not yet implemented");
    }

    public Context createSubcontext(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method createSubcontext not yet implemented");
    }

    public Object lookupLink(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method lookupLink not yet implemented");
    }

    public Object lookupLink(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method lookupLink not yet implemented");
    }

    public NameParser getNameParser(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Method getNameParser not yet implemented");
    }

    public NameParser getNameParser(String name) throws NamingException
    {
        throw new UnsupportedOperationException("Method getNameParser not yet implemented");
    }

    public Name composeName(Name name, Name prefix) throws NamingException
    {
        throw new UnsupportedOperationException("Method composeName not yet implemented");
    }

    public String composeName(String name, String prefix) throws NamingException
    {
        throw new UnsupportedOperationException("Method composeName not yet implemented");
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        throw new UnsupportedOperationException("Method addToEnvironment not yet implemented");
    }

    public Object removeFromEnvironment(String propName) throws NamingException
    {
        throw new UnsupportedOperationException("Method removeFromEnvironment not yet implemented");
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        throw new UnsupportedOperationException("Method getEnvironment not yet implemented");
    }

    public void close() throws NamingException
    {
        throw new UnsupportedOperationException("Method close not yet implemented");
    }

    public String getNameInNamespace() throws NamingException
    {
        throw new UnsupportedOperationException("Method getNameInNamespace not yet implemented");
    }
}
