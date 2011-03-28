package org.mobicents.servlet.sip.weld.extension;

import javax.servlet.sip.SipServletRequest;

public class SipServletRequestEvent {

	private SipServletRequest sipServletRequest;
	
	public SipServletRequestEvent(SipServletRequest request) {
		this.sipServletRequest = request;
	}
	
	public SipServletRequest getSipServletRequest(){
		return sipServletRequest;
	}
}
