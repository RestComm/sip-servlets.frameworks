package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.mobicents.servlet.sip.weld.extension.context.sip.SipApplicationSessionContextImpl;
import org.mobicents.servlet.sip.weld.extension.context.sip.SipSessionContextImpl;

public class SipServletsRegisterContexts implements Extension{

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		abd.addContext(new SipSessionContextImpl());
		abd.addContext(new SipApplicationSessionContextImpl());
	}
}
