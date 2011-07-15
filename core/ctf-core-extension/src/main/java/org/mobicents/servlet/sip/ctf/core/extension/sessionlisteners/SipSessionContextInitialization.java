package org.mobicents.servlet.sip.ctf.core.extension.sessionlisteners;

import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;

import org.mobicents.servlet.sip.ctf.core.extension.context.sip.SipSessionContext;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

//@SipListener
public class SipSessionContextInitialization implements SipSessionListener{

	private static SipSessionContext sipSessionContext;
//	private static SipSession sipSession;
	
	public static SipSessionContext getSipSessionContext(){
		return sipSessionContext;
	}
	
//	@Override
	public void sessionCreated(SipSessionEvent arg0) {
//		sipSessionContext = SipSessionContextImpl.getInstance();
//		sipSession = arg0.getSession();
	}

//	@Override
	public void sessionDestroyed(SipSessionEvent arg0) {
//		sipSessionContext.destroy(arg0.getSession());
	}

//	@Override
	public void sessionReadyToInvalidate(SipSessionEvent arg0) {
//		sipSessionContext.invalidate();
//		sipSessionContext.destroy(arg0.getSession());
	}
}
