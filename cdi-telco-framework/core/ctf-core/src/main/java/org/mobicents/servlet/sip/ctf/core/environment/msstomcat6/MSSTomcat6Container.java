/**
 * 
 */
package org.mobicents.servlet.sip.ctf.core.environment.msstomcat6;

import org.jboss.weld.environment.AbstractContainer;
import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.jboss.weld.environment.tomcat.WeldForwardingAnnotationProcessor;
import org.mobicents.servlet.sip.startup.ConvergedApplicationContextFacade;

/**
 * @author gvagenas 
 * gvagenas@gmail.com
 * 
 */
public class MSSTomcat6Container extends AbstractContainer
{
	public static Container INSTANCE = new MSSTomcat6Container();

	protected String classToCheck()
	{
		return "org.apache.catalina.core.ApplicationContextFacade";
	}

	public void initialize(ContainerContext context)
	{
		try
		{
			SipWeldForwardingAnnotationProcessor.replaceAnnotationProcessor(context.getEvent(), context.getManager());
			log.info("MSS 1.x on Tomcat 6 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
		}
		catch (Exception e)
		{
			log.error("Unable to replace MSS 1.x - Tomcat AnnotationProcessor. CDI injection will not be available in Servlets, Filters, or Listeners", e);
		}
	}

	@Override
	public void destroy(ContainerContext context)
	{
		SipWeldForwardingAnnotationProcessor.restoreAnnotationProcessor(context.getEvent());
	}
	
	@Override
	public boolean touch(ContainerContext context) throws Exception {
		boolean result = super.touch(context);
		if (!(context.getContext() instanceof ConvergedApplicationContextFacade)){
			result = false;
		}
		return result;
	}
}