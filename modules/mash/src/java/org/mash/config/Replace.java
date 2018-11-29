package org.mash.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Replace", propOrder = {
        "paramName",
        "value",
        "date",
        "response"
        }, namespace = "https://github.com/primatephor/mash/schema/V1")
public class Replace extends BaseParameter
{
    @XmlAttribute
    protected String search;

    @XmlAttribute
    protected String trim;

    public Replace()
    {
    }

    public Replace(String search, String value)
    {
        this.search = search;
        this.setValue(value);
    }

    /**
     * Gets the value of the search property.
     *
     * @return possible object is {@link String }
     */
    public String getSearch()
    {
        return search;
    }

    /**
     * Sets the value of the search property.
     *
     * @param value allowed object is {@link String }
     */
    public void setSearch(String value)
    {
        this.search = value;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public boolean shouldTrim()
    {
        return Boolean.valueOf(getTrim());
    }
}
