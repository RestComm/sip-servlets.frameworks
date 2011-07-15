package org.mobicents.servlet.sip.ctf.examples;

import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;

import org.apache.log4j.Logger;
import org.mobicents.framework.servlet.sip.components.SipEndpointContainer;
import org.mobicents.framework.servlet.sip.services.beans.RegistrationBean;
import org.mobicents.servlet.sip.weld.extension.event.request.Register;

public class SipRegistarClient {

	private static final long serialVersionUID = -3118672071188405646L;

	private static Logger logger = Logger.getLogger(SipRegistarClient.class);
	
	@Inject
	RegistrationBean sipRegistar;

	@Inject
	SipEndpointContainer users;

	@Inject
	SipFactory sipFactory;

	protected void doRegister(@Observes @Register SipServletRequest req) throws ServletException, IOException {
		String from = req.getFrom().getURI().toString();
		
		if (users.contains(from)){
			logger.info("The user: "+from+" is already in the users map. This is deregistration request");
		} else {
			logger.info("Received REGISTER request from user: "+from);
		}
		sipRegistar.doRegister(req);
		
	}

}
