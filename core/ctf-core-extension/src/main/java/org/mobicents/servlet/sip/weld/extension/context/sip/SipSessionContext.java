package org.mobicents.servlet.sip.weld.extension.context.sip;

import javax.enterprise.context.spi.Context;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.BoundContext;
import org.jboss.weld.context.SessionContext;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
* Based on org.jboss.weld.context.http.HttpSessionContext
*/

public interface SipSessionContext extends BoundContext<SipServletRequest>, SessionContext 
{
	
	public void invalidate();
	
	public boolean destroy(SipSession session);

}
