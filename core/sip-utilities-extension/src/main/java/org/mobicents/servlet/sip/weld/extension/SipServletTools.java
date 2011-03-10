package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;

import org.mobicents.servlet.sip.weld.extension.qualifiers.SipFactoryBean;


public class SipServletTools extends SipServlet {

	private SipFactory sipFactory;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		sipFactory = (SipFactory)getServletContext().getAttribute(SIP_FACTORY);
	}
	
	@Produces @Named @SipFactoryBean
	public SipFactory getSipFactory(){
		sipFactory = (SipFactory)getServletContext().getAttribute(SIP_FACTORY);
		return sipFactory;
	}
	
}
