/**
 * 
 */

package com.crownpartners.cq.quickstart.core.annotations;

/**
 * @author sean.steimer
 */
public enum EscapeValue {

    NONE("None"), XML("Xml"), HTML("Html"), URL_ENCODE("Url");

    private String value;

    /**
     * Construct the EscapeValue.
     * 
     * @param value
     *            the String value
     */
    EscapeValue(final String value) {
        this.value = value;
    }

    /**
     * get the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
