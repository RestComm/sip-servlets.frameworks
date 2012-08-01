package org.mobicents.servlet.sip.ctf.core.extension.event.context;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Qualifies observer method parameters to select events that fire when HTTP artifacts are being destroyed.
 * 
 * The event parameter can be a {@link javax.servlet.ServletContext}, {@link javax.servlet.ServletRequest} or a
 * {@link javax.servlet.http.HttpSession}.
 * 
 * @author Nicklas Karlsson
 */
@Qualifier
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface Destroyed {
}