
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.q4s.dafobi.jaxb.common.Documentation;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.borlas.bus.kernel.intro.form package. 
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

    private final static QName _Tabindex_QNAME = new QName("urn:q4s:dafobi:dataform", "tabindex");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.borlas.bus.kernel.intro.form
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link Documentation }
     * 
     */
    public Documentation createDocumentation() {
        return new Documentation();
    }

    /**
     * Create an instance of {@link FieldState }
     * 
     */
    public FieldState createFieldState() {
        return new FieldState();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link Column }
     * 
     */
    public Column createColumn() {
        return new Column();
    }

    /**
     * Create an instance of {@link Operations }
     * 
     */
    public Operations createDbqueries() {
        return new Operations();
    }

    /**
     * Create an instance of {@link Dataform }
     * 
     */
    public Dataform createBusform() {
        return new Dataform();
    }

    /**
     * Create an instance of {@link Field.Column }
     * 
     */
    public Field.Column createFieldColumn() {
        return new Field.Column();
    }

    /**
     * Create an instance of {@link Field.Parameter }
     * 
     */
    public Field.Parameter createFieldParameter() {
        return new Field.Parameter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:q4s:dafobi:dataform", name = "tabindex")
    public JAXBElement<Integer> createTabindex(Integer value) {
        return new JAXBElement<Integer>(_Tabindex_QNAME, Integer.class, null, value);
    }

}
