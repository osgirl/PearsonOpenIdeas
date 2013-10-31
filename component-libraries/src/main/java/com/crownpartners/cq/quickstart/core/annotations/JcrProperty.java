
package com.crownpartners.cq.quickstart.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Property Annotation is used on a component model class to automatically
 * bind a component property, generally set in an authoring dialog, into a
 * component model class field.
 * 
 * @author sean.steimer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JcrProperty {

    /**
     * The name can be used to override the property to be retrieved and bound.
     * If it's blank, the name is assumed to be the same as the field's name.
     * 
     * @return
     */
    String name() default "";

    /**
     * Whether or not this is an inherited property.
     * 
     * @return
     */
    boolean inherited() default false;

    /**
     * Whether or not this is a page property.
     * 
     * @return
     */
    boolean pageProperty() default false;

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    String[] defaultValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    int[] defaultIntValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    char[] defaultCharValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    long[] defaultLongValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    boolean[] defaultBooleanValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    double[] defaultDoubleValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    short[] defaultShortValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    byte[] defaultByteValue() default { };

    /**
     * The default value can be used to provide single or multi-value defaults
     * which are used if no property is found.
     * 
     * @return
     */
    float[] defaultFloatValue() default { };

    /**
     * How this property should be escaped.
     * 
     * @return
     */
    EscapeValue escape() default EscapeValue.NONE;

}
