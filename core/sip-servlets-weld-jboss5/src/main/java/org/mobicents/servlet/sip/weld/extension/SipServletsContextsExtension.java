package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

public class SipServletsContextsExtension implements Extension{

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		
//		ContextHolder<? extends Context> sipSessionContext = new ContextHolder<SipSessionContext>(new SipSessionContextImpl(), SipSessionContext.class, SipLiteral.INSTANCE);
//		ContextHolder<? extends Context> sipApplicationSessionContext = new ContextHolder<SipApplicationSessionContext>(new SipApplicationSessionContextImpl(), SipApplicationSessionContext.class, SipLiteral.INSTANCE);
//		
//		abd.addContext(sipSessionContext.getContext());
//		abd.addContext(sipApplicationSessionContext.getContext());
		
//		SipSessionContext sipSessionContext = new SipSessionContextImpl();
//		SipApplicationSessionContext sipApplicationSessionContext = new SipApplicationSessionContextImpl();

//		SipSessionContext sipSessionContext = SipSessionContextImpl.getInstance();
//		SipApplicationSessionContext sipApplicationSessionContext = SipApplicationSessionContextImpl.getInstance();
//		
//		abd.addContext(sipSessionContext);
//		abd.addContext(sipApplicationSessionContext);
		
	
//		sipSessionContext.activate();
		
	}
	
//	void processObserverMethod(@Observes ProcessObserverMethod<T, X> pom, BeanManager bm){
//		//Check here that we don't have duplicatate Observers for the same event payload
//	}
}
