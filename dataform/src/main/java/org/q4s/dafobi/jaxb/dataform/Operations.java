
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class содержащий запросы (в том числе к БД), которые форма может
 * выполнять в разные периоды своей работы.
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@formatter:off
@XmlType(name = "", propOrder = {
    "preselect",
    "select",
    "postselect",
    "existsrow",
    "presave",
    "checkrow",
    "insertrow",
    "updaterow",
    "deleterow",
    "postsave"
})
//@formatter:on
@XmlRootElement(name = "operations")
public class Operations {

	protected String preselect;

	/**
	 * Операции могут быть реализованы на разных движках. Здесь задается, для
	 * какого именно движка указываются операции.
	 */
	@XmlAttribute(name = "type", required = true)
	protected String type;

	@XmlElement(required = true)
	protected String select;

	protected String postselect;

	protected String existsrow;

	protected String presave;

	protected String checkrow;

	protected String insertrow;

	protected String updaterow;

	protected String deleterow;

	protected String postsave;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the value of the preselect property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPreselect() {
		return preselect;
	}

	/**
	 * Sets the value of the preselect property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPreselect(String value) {
		this.preselect = value;
	}

	/**
	 * Gets the value of the select property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSelect() {
		return select;
	}

	/**
	 * Sets the value of the select property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSelect(String value) {
		this.select = value;
	}

	/**
	 * Gets the value of the postselect property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostselect() {
		return postselect;
	}

	/**
	 * Sets the value of the postselect property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostselect(String value) {
		this.postselect = value;
	}

	/**
	 * Gets the value of the existsrow property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExistsrow() {
		return existsrow;
	}

	/**
	 * Sets the value of the existsrow property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExistsrow(String value) {
		this.existsrow = value;
	}

	/**
	 * Gets the value of the presave property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPresave() {
		return presave;
	}

	/**
	 * Sets the value of the presave property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPresave(String value) {
		this.presave = value;
	}

	/**
	 * Gets the value of the checkrow property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCheckrow() {
		return checkrow;
	}

	/**
	 * Sets the value of the checkrow property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCheckrow(String value) {
		this.checkrow = value;
	}

	/**
	 * Gets the value of the insertrow property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInsertrow() {
		return insertrow;
	}

	/**
	 * Sets the value of the insertrow property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInsertrow(String value) {
		this.insertrow = value;
	}

	/**
	 * Gets the value of the updaterow property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUpdaterow() {
		return updaterow;
	}

	/**
	 * Sets the value of the updaterow property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUpdaterow(String value) {
		this.updaterow = value;
	}

	/**
	 * Gets the value of the deleterow property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeleterow() {
		return deleterow;
	}

	/**
	 * Sets the value of the deleterow property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDeleterow(String value) {
		this.deleterow = value;
	}

	/**
	 * Gets the value of the postsave property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostsave() {
		return postsave;
	}

	/**
	 * Sets the value of the postsave property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostsave(String value) {
		this.postsave = value;
	}

}
