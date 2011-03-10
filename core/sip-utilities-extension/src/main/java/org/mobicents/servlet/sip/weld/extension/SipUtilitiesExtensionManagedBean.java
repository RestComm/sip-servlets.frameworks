package org.mobicents.servlet.sip.weld.extension;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;
import org.mobicents.servlet.sip.weld.extension.qualifiers.SipFactoryBean;

import org.apache.log4j.Logger;

public class SipUtilitiesExtensionManagedBean extends SipServlet {

	private static transient Logger logger = Logger.getLogger(SipUtilitiesExtensionManagedBean.class);

	//	SipFactory sipFactory;
	SipSessionsUtil sipSessionsUtil;
	TimerService timerService;

	//	@Inject
	//	private ServletContext servletContext;

	private SipFactory sipFactory;

//	@Produces @Named @SipFactoryBean 
//	public SipFactory getSipFactory() throws ServletException{
//		SipFactory sipFactoryJndi = null;
//		// Getting the Sip factory from the JNDI Context
//		sipFactoryJndi = (SipFactory) this.getServletConfig().getServletContext().getAttribute(SIP_FACTORY);
//		logger.info("SipFactory injected resource" + sipFactory);
//		return sipFactoryJndi;
//	}

	//	public void getaAAASipFactory() throws NamingException{
	//		SipFactory sipFactoryJndi;
	//		SipSessionsUtil sipSessionsUtilJndi;
	//		TimerService timerServiceJndi;
	//		try { 			
	//			// Getting the Sip factory from the JNDI Context
	//			Properties jndiProps = new Properties();			
	//			Context initCtx = new InitialContext(jndiProps);
	//			Context envCtx = (Context) initCtx.lookup("java:comp/env");
	//			sipFactoryJndi = (SipFactory) envCtx.lookup("sip/org.mobicents.servlet.sip.testsuite.AnnotatedApplication/SipFactory");
	//			sipSessionsUtilJndi = (SipSessionsUtil) envCtx.lookup("sip/org.mobicents.servlet.sip.testsuite.AnnotatedApplication/SipSessionsUtil");
	//			timerServiceJndi = (TimerService) envCtx.lookup("sip/org.mobicents.servlet.sip.testsuite.AnnotatedApplication/TimerService");
	//			logger.info("SipFactory injected resource" + sipFactory);
	//			logger.info("SipSessionsUtil injected resource" + sipSessionsUtil);
	//			logger.info("TimerService injected resource" + timerService);
	//			logger.info("Sip Factory ref from JNDI : " + sipFactory);
	//		} catch (NamingException e) {
	//			throw new ServletException("Uh oh -- JNDI problem !", e);
	//		}
	//		if(sipFactoryJndi == null || sipSessionsUtilJndi == null || timerServiceJndi == null) {
	//			throw new ServletException("Impossible to get one of the annotated resource");
	//		}



}
