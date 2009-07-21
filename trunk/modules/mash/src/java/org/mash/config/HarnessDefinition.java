package org.mash.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlTransient
public class HarnessDefinition
{
    @XmlElement(name = "Configuration", required = true)
    protected List<Configuration> configuration;
    @XmlElement(name = "Parameter", required = true)
    protected List<Parameter> parameter;
    @XmlAttribute
    protected String type;
    @XmlAttribute
    protected String name;

    public List<Configuration> getConfiguration()
    {
        if (configuration == null)
        {
            configuration = new ArrayList<Configuration>();
        }
        return this.configuration;
    }

    public List<Parameter> getParameter()
    {
        if (parameter == null)
        {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String value)
    {
        this.type = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
