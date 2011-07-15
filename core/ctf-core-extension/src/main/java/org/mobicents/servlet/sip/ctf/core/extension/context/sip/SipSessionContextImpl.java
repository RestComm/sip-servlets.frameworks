package org.mobicents.servlet.sip.ctf.core.extension.context.sip;

import java.lang.annotation.Annotation;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.AbstractBoundContext;
import org.jboss.weld.context.beanstore.NamingScheme;
import org.jboss.weld.context.beanstore.SimpleNamingScheme;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.beanstore.sipsession.LazySipSessionBeanStore;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

public class SipSessionContextImpl  extends AbstractBoundContext<SipServletRequest> implements SipSessionContext 
{

	private static final String IDENTIFIER = SipSessionContextImpl.class.getName();

	private final NamingScheme namingScheme;
	private static SipSessionContext instance = new SipSessionContextImpl();

	public SipSessionContextImpl()
	{
		super(true);
		this.namingScheme = new SimpleNamingScheme(SipSessionContext.class.getName());
	}

	public static SipSessionContext getInstance(){
		return instance;
	}
	
//	@Override
	public boolean associate(SipServletRequest request) {
		if (request.getAttribute(IDENTIFIER) == null)
		{
			// Don't reassociate
			request.setAttribute(IDENTIFIER, IDENTIFIER);
			setBeanStore(new LazySipSessionBeanStore(request, namingScheme));
			return true;
		}
		else
		{
			return false;
		}
	}

//	@Override
	public boolean dissociate(SipServletRequest request) {
		if (request.getAttribute(IDENTIFIER) != null)
		{
			try
			{
				setBeanStore(null);
				request.removeAttribute(IDENTIFIER);
				return true;
			}
			finally
			{
				cleanup();
			}
		}
		else
		{
			return false;
		}
	}

//	@Override
	public Class<? extends Annotation> getScope() {
		return SipSessionScoped.class;
	}

//	@Override
	public boolean destroy(SipSession session)
	{
		invalidate();
		setBeanStore(null);
		super.cleanup();
		return true;
		
//		if (getBeanStore() == null)
//		{
//			try
//			{
//				setBeanStore(new EagerSipSessionBeanStore(namingScheme, session));
//				activate();
//				invalidate();
//				deactivate();
//				setBeanStore(null);
//				return true;
//			}
//			finally
//			{
//				cleanup();
//			}
//		}
//		else
//		{
//			invalidate();
//			setBeanStore(null);
//			return false;
//		}
	}
}
