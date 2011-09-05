/**
 * 
 */
package org.mobicents.servlet.sip.ctf.core.environment.services;

import java.lang.reflect.Field;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.injection.spi.helpers.AbstractResourceServices;

/**
 * @author gvagenas 
 * gvagenas@gmail.com
 * 
 * Sip Servlets custom ResourceInjectionService.
 * Issue 2822 - http://code.google.com/p/mobicents/issues/detail?id=2822
 * 
 * First tries to resolve resources from the ServletContext and if not found delegates to the JNDI context
 * 
 */
public class SipServletResourceInjectionServices extends AbstractResourceServices implements ResourceInjectionServices
{
	private Context context;
	private ServletContext servletContext;

	public SipServletResourceInjectionServices(ServletContext servletContext)
	{
		try
		{
			context = new InitialContext();
			this.servletContext = servletContext;		
		}
		catch (NamingException e)
		{
			throw new IllegalStateException("Error creating JNDI context", e);
		}
	}

	@Override
	protected Context getContext()
	{
		return context;
	}


	@Override
	public Object resolveResource(InjectionPoint injectionPoint){

		Field field = null;
		Object objectToInject = null;
		
		if (injectionPoint.getMember() instanceof Field)
		{
			field = (Field) injectionPoint.getMember();
		}

		if (field != null){
			String typeName = field.getType().getCanonicalName();
			boolean sipField = typeName.startsWith("javax.servlet.sip");
			if (sipField){
				objectToInject = servletContext.getAttribute(typeName);
			} else {
				objectToInject = super.resolveResource(injectionPoint);
			}
		} else {
			objectToInject = super.resolveResource(injectionPoint);
		}
		
		return objectToInject;
	}

}