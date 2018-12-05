
package org.mash.config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mash.config package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Script_QNAME = new QName("https://github.com/primatephor/mash/schema/V1", "Script");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mash.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Suite }
     * 
     */
    public Suite createSuite() {
        return new Suite();
    }

    /**
     * Create an instance of {@link Parallel }
     * 
     */
    public Parallel createParallel() {
        return new Parallel();
    }

    /**
     * Create an instance of {@link Script }
     * 
     */
    public Script createScript() {
        return new Script();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link Replace }
     * 
     */
    public Replace createReplace() {
        return new Replace();
    }

    /**
     * Create an instance of {@link Configuration }
     * 
     */
    public Configuration createConfiguration() {
        return new Configuration();
    }

    /**
     * Create an instance of {@link Teardown }
     * 
     */
    public Teardown createTeardown() {
        return new Teardown();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link Run }
     * 
     */
    public Run createRun() {
        return new Run();
    }

    /**
     * Create an instance of {@link Setup }
     * 
     */
    public Setup createSetup() {
        return new Setup();
    }

    /**
     * Create an instance of {@link Verify }
     * 
     */
    public Verify createVerify() {
        return new Verify();
    }

    /**
     * Create an instance of {@link Date }
     * 
     */
    public Date createDate() {
        return new Date();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Script }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://github.com/primatephor/mash/schema/V1", name = "Script")
    public JAXBElement<Script> createScript(Script value) {
        return new JAXBElement<Script>(_Script_QNAME, Script.class, null, value);
    }

}
