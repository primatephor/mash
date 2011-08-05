package org.mash.config;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response", propOrder = {
        "value"
}, namespace = "http://code.google.com/p/mash/schema/V1")
public class Response
{

    @XmlValue
    protected String value;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected Boolean trim;

    public Response()
    {
    }

    public Response(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getTrim()
    {
        return trim;
    }

    public void setTrim(final Boolean trim)
    {
        this.trim = trim;
    }
}
