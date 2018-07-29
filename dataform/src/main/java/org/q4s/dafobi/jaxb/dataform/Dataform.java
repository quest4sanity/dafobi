
package org.q4s.dafobi.jaxb.dataform;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import org.q4s.dafobi.jaxb.common.Documentation;


/**
 * <p>Java class для описания структуры формы данных. 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataform", propOrder = {
//@formatter:off
    "documentation",
    "parameters",
    "operations",
    "columns",
    "fields"
//@formatter:on
})
@XmlRootElement
//@XmlRootElement(name = "dataform", namespace="urn:q4s:dafobi:dataform")
public class Dataform {
    
    /**
     * Символьный код формы
     */
    @XmlAttribute(name = "code", required = true)
    protected String code;
    
//    @XmlAttribute(name = "target")
//    protected String target;
//    
//    @XmlAttribute(name = "type")
//    protected String type;
//    
//    @XmlAttribute(name = "parent")
//    protected String parent;

	/** 
	 * Человекоориентированное документирование формы. 
	 */
    @XmlElement(name = "documentation", namespace = "urn:q4s:dafobi:common")
    protected Documentation documentation;
    
    /**
     * Операции, которые могут делаться с формой. 
     */
    protected Operations operations;
    
    /**
     * Параметры, которые ожидает форма для своей работы.
     */
    @XmlElementWrapper
    @XmlElement(name = "parameter", namespace = "urn:q4s:dafobi:dataform")
    protected List<Parameter> parameters;
    
    /**
     * Колонки, содержащиеся в таблице данных формы.
     */
    @XmlElementWrapper
    @XmlElement(name = "column", namespace = "urn:q4s:dafobi:dataform", required = true)
    protected List<Column> columns;
    
    /**
     * Поля, определенные для формы.
     */
    @XmlElementWrapper
    @XmlElement(name = "field", namespace = "urn:q4s:dafobi:dataform")
    protected List<Field> fields;

    /**
     * Gets the value of the documentation property.
     * 
     * @return
     *     possible object is
     *     {@link Documentation }
     *     
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Sets the value of the documentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documentation }
     *     
     */
    public void setDocumentation(Documentation value) {
        this.documentation = value;
    }

    /**
     * Gets the value of the dbqueries property.
     * 
     * @return
     *     possible object is
     *     {@link Operations }
     *     
     */
    public Operations getOperations() {
        return operations;
    }

    /**
     * Sets the value of the dbqueries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operations }
     *     
     */
    public void setOperations(Operations value) {
        this.operations = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

//    /**
//     * Gets the value of the target property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getTarget() {
//        return target;
//    }
//
//    /**
//     * Sets the value of the target property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setTarget(String value) {
//        this.target = value;
//    }
//
//    /**
//     * Gets the value of the type property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getType() {
//        return type;
//    }
//
//    /**
//     * Sets the value of the type property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setType(String value) {
//        this.type = value;
//    }
//
//    /**
//     * Gets the value of the parent property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getParent() {
//        return parent;
//    }
//
//    /**
//     * Sets the value of the parent property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setParent(String value) {
//        this.parent = value;
//    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Field> getFields() {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
