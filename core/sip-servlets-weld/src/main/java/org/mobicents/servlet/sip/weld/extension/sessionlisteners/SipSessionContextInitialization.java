package org.mobicents.servlet.sip.weld.extension.sessionlisteners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.annotation.SipListener;

import org.jboss.weld.servlet.api.ServletListener;
import org.mobicents.servlet.sip.weld.extension.context.sip.SipSessionContext;
import org.mobicents.servlet.sip.weld.extension.context.sip.SipSessionContextImpl;


@SipListener
public class SipSessionContextInitialization implements SipSessionListener{

	private static SipSessionContext sipSessionContext;
//	private static SipSession sipSession;
	
	public static SipSessionContext getSipSessionContext(){
		return sipSessionContext;
	}
	
	@Override
	public void sessionCreated(SipSessionEvent arg0) {
//		sipSessionContext = SipSessionContextImpl.getInstance();
//		sipSession = arg0.getSession();
	}

	@Override
	public void sessionDestroyed(SipSessionEvent arg0) {
//		sipSessionContext.destroy(arg0.getSession());
	}

	@Override
	public void sessionReadyToInvalidate(SipSessionEvent arg0) {
//		sipSessionContext.invalidate();
//		sipSessionContext.destroy(arg0.getSession());
	}
}
