package org.mobicents.servlet.sip.weld.extension.context.sip;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.BoundContext;
import org.jboss.weld.context.ConversationContext;

public interface SipConversationContext extends BoundContext<SipServletRequest>, ConversationContext 
{

	public boolean destroy(SipSession session);
	
}
