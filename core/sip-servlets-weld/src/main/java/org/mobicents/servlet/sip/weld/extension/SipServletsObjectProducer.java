package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;

/*
 * Makes injectable the SipFactory, SipSessionsUtil, TimerService and ServletContext
 * 
 * @author gvagenas 
 * gvagenas@gmail.com / devrealm.org
 */

public class SipServletsObjectProducer {

	@Inject
	private SipServletObjectsHolder holder;
	
    @Produces
    @ApplicationScoped
    protected ServletContext getServletContext() {
        return holder.getServletContext();
    }
	
    @Produces
    @ApplicationScoped
    protected SipFactory getSipFactory(){
    	SipFactory sipFactory = (SipFactory) holder.getServletContext().getAttribute(SipServlet.SIP_FACTORY);
    	return sipFactory;
    }
    
    @Produces
    @ApplicationScoped
    protected TimerService getTimerService(){
    	TimerService timerService = (TimerService) holder.getServletContext().getAttribute(SipServlet.TIMER_SERVICE);
    	return timerService;
    }
    
    @Produces
    @ApplicationScoped
    protected SipSessionsUtil getSipSessionsUtil(){
    	SipSessionsUtil sipSessionsUtil = (SipSessionsUtil) holder.getServletContext().getAttribute(SipServlet.SIP_SESSIONS_UTIL);
    	return sipSessionsUtil;
    }
}
