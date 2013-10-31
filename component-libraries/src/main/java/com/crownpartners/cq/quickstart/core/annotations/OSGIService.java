
package com.crownpartners.cq.quickstart.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The InjectService annotation is a marker annotation used to indicate that
 * given field in a component model class should be retrieved as an OSGI
 * service.
 * 
 * @author sean.steimer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OSGIService {

}
