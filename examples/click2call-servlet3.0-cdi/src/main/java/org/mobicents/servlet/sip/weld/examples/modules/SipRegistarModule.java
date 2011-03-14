package org.mobicents.servlet.sip.weld.examples.modules;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;

import org.apache.log4j.Logger;

@ApplicationScoped
@javax.servlet.sip.annotation.SipServlet(loadOnStartup=1, applicationName="ClickToCallAsyncApplication-CDI")
public class SipRegistarModule extends SipServlet{

	private static Logger logger = Logger.getLogger(SipRegistarModule.class);
	private static final String CONTACT_HEADER = "Contact";


	//Produces the users map to make it available for the StatusServlet
	@Produces
	HashMap<String, String> users = new HashMap<String, String>();
	
	SipFactory sipFactory;
	
	@Override
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
