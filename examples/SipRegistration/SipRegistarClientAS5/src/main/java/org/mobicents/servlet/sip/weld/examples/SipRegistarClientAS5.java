package org.mobicents.servlet.sip.weld.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;

import javax.enterprise.inject.spi.Bean;
import org.apache.log4j.Logger;


@javax.servlet.sip.annotation.SipServlet(loadOnStartup=1, applicationName="SipRegistarClientAS5")
public class SipRegistarClientAS5 extends SipServlet implements Servlet {

	private static final long serialVersionUID = -3118672071188405646L;

	private static Logger logger = Logger.getLogger(SipRegistarClientAS5.class);

	@Inject 
	SipRegistar sipRegistar; 

	@Inject @Named("users")
	HashMap<String, String> users;
	
	@Inject
	BeanManager beanManager;

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
		
		logger.info("Start printing the beans inside the BeanManager");
		
		Set<Bean<?>> allBeans = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {});
		
		for (Bean<?> bean : allBeans) {
			logger.info("Registered bean name: "+bean.getName());
		}
		
		String from = req.getFrom().getURI().toString();
		if (users!=null && users.containsKey(from)){
			logger.info("The user: "+from+" is already in the users map. This is deregistration request");
		} else {
			logger.info("users is null!");
		}
		if (sipRegistar != null) {
			sipRegistar.doRegister(req);
		} else {
			logger.info("sipRegistar is null");
		}
		
	}
}
