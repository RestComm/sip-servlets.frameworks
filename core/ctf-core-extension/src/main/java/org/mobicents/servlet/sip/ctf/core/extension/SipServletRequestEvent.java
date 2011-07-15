package org.mobicents.servlet.sip.ctf.core.extension;

import javax.servlet.sip.SipServletRequest;

/*
 * @author gvagenas 
 * gvagenas@gmail.com / devrealm.org
 */

public class SipServletRequestEvent {

	private SipServletRequest sipServletRequest;
	
	public SipServletRequestEvent(SipServletRequest request) {
		this.sipServletRequest = request;
	}
	
	public SipServletRequest getSipServletRequest(){
		return sipServletRequest;
	}
}
