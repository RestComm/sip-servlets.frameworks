package org.mobicents.servlet.sip.weld.extension.context.sip;

import java.lang.annotation.Annotation;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.AbstractBoundContext;
import org.jboss.weld.context.AbstractSharedContext;
import org.jboss.weld.context.beanstore.NamingScheme;
import org.jboss.weld.context.beanstore.SimpleNamingScheme;
import org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipappsession.EagerSipApplicationSessionBeanStore;
import org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipappsession.LazySipApplicationSessionBeanStore;
import org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipsession.EagerSipSessionBeanStore;
import org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipsession.LazySipSessionBeanStore;

public class SipApplicationSessionContextImpl extends AbstractBoundContext<SipServletRequest> implements SipApplicationSessionContext 
{

	private static final String IDENTIFIER = SipApplicationSessionContextImpl.class.getName();

	private final NamingScheme namingScheme;
	private static SipApplicationSessionContext instance = new SipApplicationSessionContextImpl();

	public SipApplicationSessionContextImpl()
	{
		super(true);
		this.namingScheme = new SimpleNamingScheme(SipApplicationSessionContext.class.getName());
	}

	
	public static SipApplicationSessionContext getInstance(){
		return instance;
	}
	
	@Override
	public boolean associate(SipServletRequest request) {
		if (request.getAttribute(IDENTIFIER) == null)
		{
			// Don't reassociate
			request.setAttribute(IDENTIFIER, IDENTIFIER);
			setBeanStore(new LazySipApplicationSessionBeanStore(request, namingScheme));
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
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

	@Override
	public Class<? extends Annotation> getScope() {
		return SipApplicationSessionScoped.class;
	}

	@Override
	public boolean destroy(SipApplicationSession session)
	{
		if (getBeanStore() == null)
		{
			try
			{
//				SipConversationContext conversationContext = getConversationContext();
				setBeanStore(new EagerSipApplicationSessionBeanStore(namingScheme, session));
				activate();
				invalidate();
//				conversationContext.destroy(session);
				deactivate();
				setBeanStore(null);
				return true;
			}
			finally
			{
				cleanup();
			}
		}
		else
		{
			invalidate();
			destroy();
			return false;
		}
	}
}