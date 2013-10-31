
package com.crownpartners.cq.quickstart.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The JcrResource Annotation is used on a component model class to
 * automatically bind a resource into a class field.
 * 
 * @author sean.steimer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JcrResource {

    /**
     * The path can be used to override the path to the resource. If it's blank,
     * the path is assumed to be the same as the field's name, relative to the
     * current resource. Paths can be relative or absolute (begin with '/').
     * 
     * @return
     */
    String path() default "";

}
