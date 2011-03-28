package org.mobicents.servlet.sip.weld.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;

import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.weld.extension.event.request.Register;

public class SipRegistarClientAS5 {

	private static final long serialVersionUID = -3118672071188405646L;

	private static Logger logger = Logger.getLogger(SipRegistarClientAS5.class);

	@Inject 
	SipRegistar sipRegistar; 

	@Inject
	HashMap<String, String> users;
	
	@Inject
	BeanManager beanManager;

	@Inject
	SipFactory sipFactory;

	protected void doRegister(@Observes @Register SipServletRequest req) throws ServletException, IOException {
		
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
