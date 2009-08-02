package org.mash.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Parameter", propOrder = {
        "value",
        "date",
        "response",
        "replace"
        }, namespace = "http://code.google.com/p/mash/schema/V1")
public class Parameter extends BaseParameter implements Replaceable
{
    @XmlElement(name = "Replace")
    protected List<Replace> replace;

    public Parameter()
    {
    }

    public Parameter(String name, String value, String file)
    {
        super(name, value, file);
    }

    public Parameter(String name, String value)
    {
        super(name, value);
    }

    public List<Replace> getReplace()
    {
        if (replace == null)
        {
            replace = new ArrayList<Replace>();
        }
        return this.replace;
    }
}
