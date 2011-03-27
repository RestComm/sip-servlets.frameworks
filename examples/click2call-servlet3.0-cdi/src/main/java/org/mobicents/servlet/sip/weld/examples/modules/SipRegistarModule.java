package org.mobicents.servlet.sip.weld.examples.modules;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.apache.log4j.Logger;

// Module responsible for the registration requests.

public class SipRegistarModule { //extends SipServlet{

	private static Logger logger = Logger.getLogger(SipRegistarModule.class);
	private static final String CONTACT_HEADER = "Contact";

	// Inject the RegisteredUsers module to keep track of registered users.
	@Inject
	RegisteredUsers users;

//	@Override
	public void doRegister(SipServletRequest req) throws ServletException, IOException {
		logger.info("Received register request: " + req.getTo());
		int response = SipServletResponse.SC_OK;
		SipServletResponse resp = req.createResponse(response);
		
		Address address = req.getAddressHeader(CONTACT_HEADER);
		String fromURI = req.getFrom().getURI().toString();
		
		int expires = address.getExpires();
		if(expires < 0) {
			expires = req.getExpires();
		}
		if(expires == 0) {
			users.remove(fromURI);
			logger.info("User " + fromURI + " unregistered");
		} else {
			resp.setAddressHeader(CONTACT_HEADER, address);
			users.put(fromURI, address.getURI().toString());
			
			logger.info("User " + fromURI + 
					" registered with an Expire time of " + expires);
		}				
		
		resp.send();
	}

	
}
