package org.mobicents.servlet.sip.weld.extension.context.sip;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;

import org.jboss.weld.context.BoundContext;
import org.jboss.weld.context.SessionContext;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/
public interface SipApplicationSessionContext extends BoundContext<SipServletRequest>, SessionContext 
{
	
	public void invalidate();
	
	public boolean destroy(SipApplicationSession session);
	

}