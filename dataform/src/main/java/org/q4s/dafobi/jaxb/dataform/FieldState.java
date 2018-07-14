
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for fieldState complex type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fieldState", propOrder = {
//@formatter:off
    "visible",
    "enabled",
    "required"
//@formatter:on
})
public class FieldState {

	protected String visible;
	
	protected String enabled;
	
	protected String required;

	/**
	 * Gets the value of the visible property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVisible() {
		return visible;
	}

	/**
	 * Sets the value of the visible property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVisible(String value) {
		this.visible = value;
	}

	/**
	 * Gets the value of the enabled property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * Sets the value of the enabled property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnabled(String value) {
		this.enabled = value;
	}

	/**
	 * Gets the value of the required property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * Sets the value of the required property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRequired(String value) {
		this.required = value;
	}

}
