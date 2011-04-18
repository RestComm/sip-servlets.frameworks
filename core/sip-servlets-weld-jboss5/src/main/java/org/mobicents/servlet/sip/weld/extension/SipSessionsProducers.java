package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

@ApplicationScoped
public class SipSessionsProducers {

	@Inject
	SipSessionsHolder holder;
	
	@Produces 
	public SipSession getSipSession(){
		return holder.getSipSession();
	}
	
	@Produces 
	public SipApplicationSession getSipApplicationSession(){
		return holder.getSipApplicationSession();
	}
}
