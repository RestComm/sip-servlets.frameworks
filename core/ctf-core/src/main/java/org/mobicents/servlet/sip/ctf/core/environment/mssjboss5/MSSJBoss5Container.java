/**
 * 
 */
package org.mobicents.servlet.sip.ctf.core.environment.mssjboss5;

import org.jboss.weld.environment.AbstractContainer;
import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.mobicents.servlet.sip.ctf.core.environment.msstomcat6.MSSTomcat6Container;
import org.mobicents.servlet.sip.ctf.core.environment.msstomcat6.SipWeldForwardingAnnotationProcessor;
import org.mobicents.servlet.sip.startup.ConvergedApplicationContextFacade;

/**
 * @author gvagenas 
 * gvagenas@gmail.com
 * 
 */
public class MSSJBoss5Container extends AbstractContainer
{
	public static Container INSTANCE = new MSSJBoss5Container();

	protected String classToCheck()
	{
		return "org.apache.InstanceManager";
	}

	public void initialize(ContainerContext context)
	{
		try
		{
			SipWeldForwardingJbossInstanceManager.replaceInstanceManager(context.getEvent(), context.getManager());
			log.info("MSS 1.x on JBoss5 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
		}
		catch (Exception e)
		{
			log.error("Unable to replace MSS 1.x - JBoss5 InstanceManager. CDI injection will not be available in Servlets, Filters, or Listeners", e);
		}
	}

	@Override
	public void destroy(ContainerContext context)
	{
		SipWeldForwardingJbossInstanceManager.restoreInstanceManager(context.getEvent());
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