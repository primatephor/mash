//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.01 at 12:03:23 PM PDT 
//


package org.mash.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Script", propOrder = {
        "tag",
        "harnesses"
        }, namespace = "http://code.google.com/p/mash/schema/V1")

@XmlRootElement(name = "Script", namespace = "http://code.google.com/p/mash/schema/V1")
public class Script implements ScriptDefinition
{

    @XmlElements({
        @XmlElement(name = "Script", type = Script.class),
        @XmlElement(name = "Setup", type = Setup.class),
        @XmlElement(name = "Run", type = Run.class),
        @XmlElement(name = "Verify", type = Verify.class),
        @XmlElement(name = "Teardown", type = Teardown.class)
            })
    protected List<Object> harnesses;

    @XmlElement(name = "Tag")
    protected List<String> tag;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String dir;
    @XmlAttribute
    protected String file;

    private File path;

    /**
     * Gets the value of the tag property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the tag property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTag().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getTag()
    {
        if (tag == null)
        {
            tag = new ArrayList<String>();
        }
        return this.tag;
    }


    public List<Object> getHarnesses()
    {
        if (harnesses == null)
        {
            harnesses = new ArrayList<Object>();
        }
        return harnesses;
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

    /**
     * Gets the value of the dir property.
     *
     * @return possible object is {@link String }
     */
    public String getDir()
    {
        return dir;
    }

    /**
     * Sets the value of the dir property.
     *
     * @param value allowed object is {@link String }
     */
    public void setDir(String value)
    {
        this.dir = value;
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

    public File getPath()
    {
        return path;
    }

    public void setPath(File path)
    {
        this.path = path;
    }
}
