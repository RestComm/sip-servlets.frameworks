package org.mobicents.servlet.sip.ctf.core.extension.context.sip;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

@Target( { TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@NormalScope(passivating = true)
@Inherited
public @interface SipSessionScoped {

}
