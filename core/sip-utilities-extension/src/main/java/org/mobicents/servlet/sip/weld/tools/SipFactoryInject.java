package org.mobicents.servlet.sip.weld.tools;

import javax.enterprise.inject.Produces;
import javax.servlet.sip.SipFactory;

import org.mobicents.servlet.sip.weld.extension.qualifiers.SipFactoryBean;

public class SipFactoryInject {
	
	private SipFactory sipFactory;
	
	@Produces @SipFactoryBean
	public SipFactory getSipFactory() {
		return sipFactory;
	}
	
	public void setSipFactory(SipFactory sipFactory){
		this.sipFactory = sipFactory;
	}
}
