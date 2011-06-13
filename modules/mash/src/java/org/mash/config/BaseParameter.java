package org.mash.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public class BaseParameter
{
    @XmlElement(name = "ParamName")
    protected Parameter paramName;
    @XmlElement(name = "Value")
    protected String value;
    @XmlElement(name = "Response")
    protected Response response;
    @XmlElement(name = "Date")
    protected Date date;

    @XmlAttribute
    protected String file;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String property;
    @XmlAttribute
    protected String scriptParameter;

    public BaseParameter()
    {
    }

    public BaseParameter(String name, String value, String file)
    {
        this.value = value;
        this.file = file;
        this.name = name;
    }

    public BaseParameter(String name, String value)
    {
        this.value = value;
        this.name = name;
    }

    public Parameter getParamName()
    {
        return paramName;
    }

    public void setParamName(Parameter paramName)
    {
        this.paramName = paramName;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Gets the value of the file property.
     *
     * @return possible object is {@link String }
     */
    public String getFile()
    {
        return file;
    }

    /**
     * Sets the value of the file property.
     *
     * @param value allowed object is {@link String }
     */
    public void setFile(String value)
    {
        this.file = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value)
    {
        this.name = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public Response getResponse()
    {
        return response;
    }

    public void setResponse(Response response)
    {
        this.response = response;
    }

    public String getScriptParameter()
    {
        return scriptParameter;
    }

    public void setScriptParameter(String scriptParameter)
    {
        this.scriptParameter = scriptParameter;
    }
}
