package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;

import org.mobicents.servlet.sip.weld.extension.context.sip.SipApplicationSessionScoped;
import org.mobicents.servlet.sip.weld.extension.context.sip.SipSessionScoped;

/*
 * SipSession and SipApplicationSession producer
 * 
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

@ApplicationScoped
public class SipSessionsProducers {

	@Inject
	SipSessionsHolder holder;
	
	@Produces //@SipSessionScoped
	public SipSession getSipSession(){
		return holder.getSipSession();
	}
	
	@Produces //@SipApplicationSessionScoped
	public SipApplicationSession getSipApplicationSession(){
		return holder.getSipApplicationSession();
	}
}
