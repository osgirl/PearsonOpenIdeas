package com.crownpartners.cq.quickstart.core.annotations.parser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.annotations.EscapeValue;
import com.crownpartners.cq.quickstart.core.annotations.JcrProperty;
import com.crownpartners.cq.quickstart.core.annotations.JcrResource;
import com.crownpartners.cq.quickstart.core.annotations.OSGIService;
import com.crownpartners.cq.quickstart.core.annotations.RequestParameter;
import com.crownpartners.cq.quickstart.core.component.AbstractComponent;

/**
 * This class parses annotations for component classes and set's up property values based on those annotations.
 * 
 * @author sean.steimer
 */
public class ComponentAnnotationParser {

    private static final Logger log = LoggerFactory.getLogger(ComponentAnnotationParser.class);
    private static final String UTF8_ENCODING = "UTF-8";

    /**
     * Parse the annotations and set property values for the component.
     * 
     * @param component
     *            the component model class.
     */
    public void parse(final AbstractComponent component) {
        final Class<?> clazz = component.getClass();
        final List<Field> fields = collectFields(clazz);
        if (fields != null) {
            for (final Field f : fields) {
                try {
                    if (f.isAnnotationPresent(JcrProperty.class)) {
                        final Object value = getPropertyValue(component, f);
                        setFieldValue(component, f, value);
                    } else if (f.isAnnotationPresent(RequestParameter.class)) {
                        final Object value = getParameterValue(component, f);
                        setFieldValue(component, f, value);
                    } else if (f.isAnnotationPresent(OSGIService.class)) {
                        final Class<?> serviceType = f.getType();
                        final Object service = component.getSlingScriptHelper().getService(serviceType);
                        setFieldValue(component, f, service);
                    } else if (f.isAnnotationPresent(JcrResource.class)) {
                        final Object resource = getResourceValue(component, f);
                        setFieldValue(component, f, resource);
                    }
                } catch (final Exception e) {
                    // catch, log, and swallow any exceptions so that other
                    // properties can still be set.
                    // TODO consider changing the behavior?
                    log.error("An exception occurred while trying to initialize properties for a component.", e);
                }
            }
        }
    }

    /**
     * @param clazz
     *            the class to find fields on
     * @return a list of fields
     */
    private List<Field> collectFields(final Class<?> clazz) {
        final List<Field> fields = new ArrayList<Field>();

        // if the class has any declared fields, add them
        final Field[] classFields = clazz.getDeclaredFields();
        if (classFields != null) {
            for (final Field f : classFields) {
                fields.add(f);
            }
        }

        final Class<?> superClass = clazz.getSuperclass();
        if (AbstractComponent.class.isAssignableFrom(superClass)) {
            final List<Field> superClassFields = collectFields(superClass);
            fields.addAll(superClassFields);
        }

        return fields;
    }

    /**
     * Get the resource object which should be assigned to the field.
     * 
     * @param component
     *            the current component, that contains the field to be assigned.
     * @param f
     *            the field containing a {@link com.crownpartners.cq.quickstart.core.annotations.JcrResource} annotation
     * @return the resource object
     */
    private Object getResourceValue(final AbstractComponent component, final Field f) {
        final String resourcePath = getResourcePath(f);
        final org.apache.sling.api.resource.Resource res = component.getResourceResolver().getResource(
                component.getCurrentResource(), resourcePath);
        if (res != null) {
            final Class<?> type = f.getType();
            if (AbstractComponent.class.isAssignableFrom(type)) {
                try {
                    final Constructor<?> ct = type.getConstructor(new Class[] { PageContext.class,
                            org.apache.sling.api.resource.Resource.class });
                    final Object o = ct.newInstance(component.getPageContext(), res);
                    return o;
                } catch (final SecurityException e) {
                    log.error("Failed to create resource object due to SecurityException.", e);
                } catch (final IllegalArgumentException e) {
                    log.error("Failed to create resource object due to IllegalArgumentException.", e);
                } catch (final NoSuchMethodException e) {
                    log.error("Failed to create resource object due to NoSuchMethodException.", e);
                } catch (final InstantiationException e) {
                    log.error("Failed to create resource object due to InstantiationException.", e);
                } catch (final IllegalAccessException e) {
                    log.error("Failed to create resource object due to IllegalAccessException.", e);
                } catch (final InvocationTargetException e) {
                    log.error("Failed to create resource object due to InvocationTargetException.", e);
                }
            }
        }

        return null;
    }

    /**
     * Get the path to the mapped resource specified by this field.
     * 
     * @param f
     *            the field containing a {@link com.crownpartners.cq.quickstart.core.annotations.JcrResource} annotation
     * @return the path to the resource.
     */
    private String getResourcePath(final Field f) {
        final JcrResource res = f.getAnnotation(JcrResource.class);
        String resPath = res.path();
        if (StringUtils.isBlank(resPath)) {
            resPath = f.getName();
        }

        return resPath;
    }

    /**
     * Get the parameter value.
     * 
     * @param component
     *            the current component
     * @param f
     *            the field to be set, used to determine the parameter name
     * @return the parameter value
     */
    private Object getParameterValue(final AbstractComponent component, final Field f) {
        final String parameterName = getParameterName(f);
        String[] values = component.getSlingRequest().getParameterValues(parameterName);
        final RequestParameter reqParameter = f.getAnnotation(RequestParameter.class);

        if (ArrayUtils.isEmpty(values)) {
            final String[] defaultVals = reqParameter.defaultValue();
            if (!ArrayUtils.isEmpty(defaultVals)) {
                values = defaultVals;
            }
        }

        if (f.getType().equals(String.class) && !ArrayUtils.isEmpty(values)) {
            String val = values[0];
            if (reqParameter.escape() != EscapeValue.NONE) {
                val = escapeStringValue(reqParameter.escape(), val);
            }
            return val;
        } else {
            if (reqParameter.escape() != EscapeValue.NONE) {
                for (int i = 0; i < values.length; i++) {
                    String val = values[i];
                    val = escapeStringValue(reqParameter.escape(), val);
                    values[i] = val;
                }
            }
            return values;
        }
    }

    /**
     * Get the name of the request parameter.
     * 
     * @param f
     *            the field to be set
     * @return the name of the parameter whose value will be set into the field
     */
    private String getParameterName(final Field f) {
        final RequestParameter reqParameter = f.getAnnotation(RequestParameter.class);
        String parameterName = reqParameter.name();
        if (StringUtils.isBlank(parameterName)) {
            parameterName = f.getName();
        }
        return parameterName;
    }

    /**
     * Set the property value on the field via reflection.
     * 
     * @param component
     *            the component class where the field will be set
     * @param f
     *            the field object that will be set
     * @param value
     *            the value to be set in the field
     * @throws IllegalAccessException
     *             if the reflection invocation throws an IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     *             if the reflection invocation throws an InvocationTargetException
     */
    private void setFieldValue(final AbstractComponent component, final Field f, final Object value)
            throws IllegalAccessException, InvocationTargetException {
        final Method setterMethod = getSetterMethod(f, component.getClass());
        if (setterMethod != null) {
            setterMethod.invoke(component, value);
        } else {
            // if no setter method is found, try setting the field directly
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            f.set(component, value);
        }
    }

    /**
     * Get the value of a component property.
     * 
     * @param component
     *            the component model class
     * @param f
     *            the field, used for determining the property name
     * @return the value of the property
     */
    private Object getPropertyValue(final AbstractComponent component, final Field f) {
        final Class<?> propObjectType = f.getType();
        final JcrProperty prop = f.getAnnotation(JcrProperty.class);
        final Object defaultValue = getDefaultValue(propObjectType, prop);

        final String propName = getPropertyName(f);
        Object value = null;
        if (defaultValue != null) {
            if (prop.inherited()) {
                if (prop.pageProperty()) {
                    value = component.getPageProperties().getInherited(propName, defaultValue);
                } else {
                    value = component.getInheritedProperties().getInherited(propName, defaultValue);
                }
            } else {
                if (prop.pageProperty()) {
                    value = component.getPageProperties().get(propName, defaultValue);
                } else {
                    value = component.getProperties().get(propName, defaultValue);
                }
            }
        } else {
            if (prop.inherited()) {
                if (prop.pageProperty()) {
                    value = component.getPageProperties().getInherited(propName, propObjectType);
                } else {
                    value = component.getInheritedProperties().getInherited(propName, propObjectType);
                }
            } else {
                if (prop.pageProperty()) {
                    value = component.getPageProperties().get(propName, propObjectType);
                } else {
                    value = component.getProperties().get(propName, propObjectType);
                }

            }
        }

        if (prop.escape() != EscapeValue.NONE) {
            if (value instanceof String) {
                final String strValue = (String) value;
                value = escapeStringValue(prop.escape(), strValue);
            } else if (value instanceof String[]) {
                final String[] strValues = (String[]) value;
                final String[] escapesStrValue = new String[strValues.length];
                for (int i = 0; i < strValues.length; i++) {
                    String strValue = strValues[i];
                    strValue = escapeStringValue(prop.escape(), strValue);
                    escapesStrValue[i] = strValue;
                }
                value = escapesStrValue;
            }
        }

        return value;
    }

    /**
     * Get the value, escapes as appropriate.
     * 
     * @param escapeType
     *            how to escape the value
     * @param strValue
     *            the string value to escape
     * @return the escaped string value
     */
    private String escapeStringValue(final EscapeValue escapeType, final String strValue) {
        String value = strValue;
        switch (escapeType) {
            case HTML:
                value = StringEscapeUtils.escapeHtml(strValue);
            case XML:
                value = StringEscapeUtils.escapeXml(strValue);
            case URL_ENCODE:
                try {
                    value = URLEncoder.encode(strValue, UTF8_ENCODING);
                } catch (final UnsupportedEncodingException e) {
                    // error trying to encode, just log and move on with life
                    log.error("Error while trying to URL Encode value: " + strValue, e);
                }
            default:
                // do nothing
        }
        return value;
    }

    /**
     * Get the default value of the property, based on the object's type.
     * 
     * @param propObjectType
     *            the type that this object will return
     * @param prop
     *            the Property annotation
     * @return the default value, if one is found of the applicable type, or null if none is found
     */
    private Object getDefaultValue(final Class<?> propObjectType, final JcrProperty prop) {
        Object defaultValue = null;
        if (propObjectType.equals(String.class)) {
            if (!ArrayUtils.isEmpty(prop.defaultValue())) {
                defaultValue = prop.defaultValue()[0];
            }
        } else if (propObjectType.equals(String[].class)) {
            if (!ArrayUtils.isEmpty(prop.defaultValue())) {
                defaultValue = prop.defaultValue();
            }
        } else if (propObjectType.equals(int.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultIntValue()))) {
                defaultValue = prop.defaultIntValue()[0];
            }
        } else if (propObjectType.equals(int[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultIntValue()))) {
                defaultValue = prop.defaultIntValue();
            }
        } else if (propObjectType.equals(char.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultCharValue()))) {
                defaultValue = prop.defaultCharValue()[0];
            }
        } else if (propObjectType.equals(char[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultCharValue()))) {
                defaultValue = prop.defaultCharValue();
            }
        } else if (propObjectType.equals(long.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultLongValue()))) {
                defaultValue = prop.defaultLongValue()[0];
            }
        } else if (propObjectType.equals(long[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultLongValue()))) {
                defaultValue = prop.defaultLongValue();
            }
        } else if (propObjectType.equals(boolean.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultBooleanValue()))) {
                defaultValue = prop.defaultBooleanValue()[0];
            }
        } else if (propObjectType.equals(boolean[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultBooleanValue()))) {
                defaultValue = prop.defaultBooleanValue();
            }
        } else if (propObjectType.equals(double.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultDoubleValue()))) {
                defaultValue = prop.defaultDoubleValue()[0];
            }
        } else if (propObjectType.equals(double[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultDoubleValue()))) {
                defaultValue = prop.defaultDoubleValue();
            }
        } else if (propObjectType.equals(short.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultShortValue()))) {
                defaultValue = prop.defaultShortValue()[0];
            }
        } else if (propObjectType.equals(short[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultShortValue()))) {
                defaultValue = prop.defaultShortValue();
            }
        } else if (propObjectType.equals(byte.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultByteValue()))) {
                defaultValue = prop.defaultByteValue()[0];
            }
        } else if (propObjectType.equals(byte[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultByteValue()))) {
                defaultValue = prop.defaultByteValue();
            }
        } else if (propObjectType.equals(float.class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultFloatValue()))) {
                defaultValue = prop.defaultFloatValue()[0];
            }
        } else if (propObjectType.equals(float[].class)) {
            if (!ArrayUtils.isEmpty(ArrayUtils.toObject(prop.defaultFloatValue()))) {
                defaultValue = prop.defaultFloatValue();
            }
        }

        return defaultValue;
    }

    /**
     * Find the setter method to set the property, if one exists.
     * 
     * @param f
     *            the field which will be set
     * @param clazz
     *            the class containing the setter method
     * @return the setter method if one exists, or null if one isn't found.
     */
    private Method getSetterMethod(final Field f, final Class<?> clazz) {
        final String fieldName = f.getName();
        final String setterMethodName = String.format("set%C%s", fieldName.charAt(0), fieldName.substring(1));
        final Class<?> propObjectType = f.getType();
        try {
            final Method method = clazz.getMethod(setterMethodName, propObjectType);
            return method;
        } catch (final NoSuchMethodException e) {
            log.warn("No setter method found for field {} on Class {}", f.getName(), clazz.getName());
            return null;
        }
    }

    /**
     * Get the name of the component property.
     * 
     * @param f
     *            the field object
     * @return the name of the component property
     */
    private String getPropertyName(final Field f) {
        final JcrProperty prop = f.getAnnotation(JcrProperty.class);
        String propName = prop.name();
        if (StringUtils.isBlank(propName)) {
            propName = f.getName();
        }

        return propName;
    }
}
