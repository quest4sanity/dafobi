
package org.q4s.dafobi.jaxb.dataform;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.q4s.dafobi.jaxb.common.Documentation;

/**
 * <p>
 * Java class for anonymous complex type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
//@formatter:off
    "documentation",
    "tabindex",
    "fieldtype",
    "parameters",
    "columns",
    "state"
//@formatter:on
})
@XmlRootElement(name = "field")
public class Field {

    @XmlElement(name = "documentation", namespace = "urn:q4s:dafobi:common", required = true)
	protected Documentation documentation;

	protected int tabindex;

	@XmlElement(required = true)
	protected String fieldtype;

	@XmlElementWrapper
	@XmlElement(name = "parameter", namespace = "urn:q4s:dafobi:dataform")
	protected List<Field.Parameter> parameters;

	@XmlElementWrapper
	@XmlElement(name = "column", namespace = "urn:q4s:dafobi:dataform")
	protected List<Field.Column> columns;

	protected FieldState state;

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
	 * Gets the value of the tabindex property.
	 * 
	 */
	public int getTabindex() {
		return tabindex;
	}

	/**
	 * Sets the value of the tabindex property.
	 * 
	 */
	public void setTabindex(int value) {
		this.tabindex = value;
	}

	/**
	 * Gets the value of the fieldtype property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFieldtype() {
		return fieldtype;
	}

	/**
	 * Sets the value of the fieldtype property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFieldtype(String value) {
		this.fieldtype = value;
	}

	/**
	 * Gets the value of the state property.
	 * 
	 * @return possible object is {@link FieldState }
	 * 
	 */
	public FieldState getState() {
		return state;
	}

	/**
	 * Sets the value of the state property.
	 * 
	 * @param value
	 *            allowed object is {@link FieldState }
	 * 
	 */
	public void setState(FieldState value) {
		this.state = value;
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

	public List<Field.Parameter> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<Field.Parameter>();
		}
		return parameters;
	}

	public void setParameters(List<Field.Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<Field.Column> getColumns() {
		if (columns == null) {
			columns = new ArrayList<Field.Column>();
		}
		return columns;
	}

	public void setColumns(List<Field.Column> columns) {
		this.columns = columns;
	}

	/**
	 * <p>
	 * Java class описания привязки колонки к полю. 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Column {

		@XmlAttribute(name = "code")
		protected String code;
		
		@XmlAttribute(name = "ref", required = true)
		protected String ref;

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

		/**
		 * Gets the value of the ref property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getRef() {
			return ref;
		}

		/**
		 * Sets the value of the ref property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setRef(String value) {
			this.ref = value;
		}

	}

	/**
	 * <p>
	 * Java class описания определения параметра для поля.
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "expression" })
	public static class Parameter {

		@XmlElement(required = true)
		protected String expression;
		
		@XmlAttribute(name = "ref", required = true)
		protected String ref;

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
		 * Gets the value of the ref property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getRef() {
			return ref;
		}

		/**
		 * Sets the value of the ref property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setRef(String value) {
			this.ref = value;
		}

	}

}
