
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.q4s.dafobi.jaxb.trans.DataType;

/**
 * <p>
 * Java class для описания колонки данных
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { 
//@formatter:off
		"group", 
		"datatype", 
		"alias", 
		"_default", 
		"expression" })
//@formatter:on
@XmlRootElement(name = "column")
public class Column {

	protected String group;

    @XmlElement(name = "datatype", namespace = "http://www.q4s.org/dafobi/trans", required = true)
	protected DataType datatype;

	protected String alias;

	@XmlElement(name = "default")
	protected String _default;

	protected String expression;

	@XmlAttribute(name = "code", required = true)
	protected String code;

	/**
	 * Gets the value of the group property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Sets the value of the group property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGroup(String value) {
		this.group = value;
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
	 * Gets the value of the alias property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the value of the alias property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAlias(String value) {
		this.alias = value;
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
	 * Gets the value of the expression property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Sets the value of the expression property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExpression(String value) {
		this.expression = value;
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
