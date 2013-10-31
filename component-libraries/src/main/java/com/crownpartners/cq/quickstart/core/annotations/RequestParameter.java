
package com.crownpartners.cq.quickstart.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The RequestParameter annotation is used on a component model class to
 * automatically bind a request parameter into a component model class field.
 * 
 * @author sean.steimer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestParameter {

    /**
     * The name can be used to override the parameter to be retrieved and bound.
     * If it's blank, the name is assumed to be the same as the field's name.
     * 
     * @return
     */
    String name() default "";

    /**
     * The default values can be used to provide a default for multi-value/array
     * parameters if no request values are found.
     * 
     * @return
     */
    String[] defaultValue() default { };

    /**
     * How this parameter should be escaped.
     * 
     * @return
     */
    EscapeValue escape() default EscapeValue.NONE;

}
