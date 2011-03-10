package org.mobicents.servlet.sip.weld.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;

import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.weld.examples.SipRegistar;


@javax.servlet.sip.annotation.SipServlet(loadOnStartup=1, applicationName="SipRegistarClient")
public class SipRegistarClient extends SipServlet implements Servlet {

	private static final long serialVersionUID = -3118672071188405646L;

	private static Logger logger = Logger.getLogger(SipRegistarClient.class);

	@Inject 
	SipRegistar sipRegistar; 

	@Inject @Named("users")
	HashMap<String, String> users;

	Map<String, List<URI>> registeredUsers = null;

	SipFactory sipFactory;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		//Get the SipFactory
		sipFactory = (SipFactory)getServletContext().getAttribute(SIP_FACTORY);

	}

	@Override
	protected void doRegister(SipServletRequest req) throws ServletException, IOException {
		String from = req.getFrom().getURI().toString();
		if (users.containsKey(from)){
			logger.info("The user: "+from+" is already in the users map. This is deregistration request");
		} else {
			logger.info("Received REGISTER request from user: "+from);
		}
		sipRegistar.doRegister(req);
		
	}

}
