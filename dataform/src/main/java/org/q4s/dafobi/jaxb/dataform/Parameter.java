
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.q4s.dafobi.jaxb.common.Documentation;
import org.q4s.dafobi.jaxb.trans.DataType;

/**
 * <p>
 * Java class for anonymous complex type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
//@formatter:off
    "documentation",
    "datatype",
    "required",
    "_default"
//@formatter:on
})
@XmlRootElement(name = "parameter")
public class Parameter {

    @XmlElement(name = "documentation", namespace = "http://www.q4s.org/dafobi/common", required = true)
	protected Documentation documentation;

    @XmlElement(name = "datatype", namespace = "http://www.q4s.org/dafobi/trans", required = true)
	protected DataType datatype;

	protected Boolean required;

	@XmlElement(name = "default")
	protected String _default;

	@XmlAttribute(name = "code", required = true)
	protected String code;

	/**
	 * Gets the value of the documentation property.
	 * 
	 * @return possible object is {@link Documentation }
	 * 
	 */
	public Documentation getDocumentation() {
		return documentation;
	}

	/**
	 * Sets the value of the documentation property.
	 * 
	 * @param value
	 *            allowed object is {@link Documentation }
	 * 
	 */
	public void setDocumentation(Documentation value) {
		this.documentation = value;
	}

	/**
	 * Gets the value of the datatype property.
	 * 
	 * @return possible object is {@link DataType }
	 * 
	 */
	public DataType getDatatype() {
		return datatype;
	}

	/**
	 * Sets the value of the datatype property.
	 * 
	 * @param value
	 *            allowed object is {@link DataType }
	 * 
	 */
	public void setDatatype(DataType value) {
		this.datatype = value;
	}

	/**
	 * Gets the value of the required property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isRequired() {
		return required;
	}

	/**
	 * Sets the value of the required property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setRequired(Boolean value) {
		this.required = value;
	}

	/**
	 * Gets the value of the default property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDefault() {
		return _default;
	}

	/**
	 * Sets the value of the default property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDefault(String value) {
		this._default = value;
	}

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCode(String value) {
		this.code = value;
	}

}
