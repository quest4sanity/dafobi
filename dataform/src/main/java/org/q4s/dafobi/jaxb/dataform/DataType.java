
package org.q4s.dafobi.jaxb.dataform;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class для описания типа данных.
 */
@XmlType(name = "dataType")
@XmlEnum
public enum DataType {

    NUMBER,
    STRING,
    TIMESTAMP,
    TIME,
    DATE,
    CLOB;

    public String value() {
        return name();
    }

    public static DataType fromValue(String v) {
        return valueOf(v);
    }

}
