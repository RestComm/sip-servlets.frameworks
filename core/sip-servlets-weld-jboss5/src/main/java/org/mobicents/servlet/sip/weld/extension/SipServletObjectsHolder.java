package org.mobicents.servlet.sip.weld.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.mobicents.servlet.sip.weld.extension.event.context.Destroyed;
import org.mobicents.servlet.sip.weld.extension.event.context.Initialized;

//From Seam3->Servlet module : org.jboss.seam.servlet.event.ImplicitServletObjectsHolder

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

@ApplicationScoped
public class SipServletObjectsHolder {

	private ServletContext servletCtx;
	
	@Inject
	private BeanManager beanManager;
	
    protected void contextInitialized(@Observes @Initialized final InternalServletContextEvent e, BeanManager beanManager) {
        ServletContext ctx = e.getServletContext();
        servletCtx = ctx;
    }
    
    protected void contextDestroyed(@Observes @Destroyed final InternalServletContextEvent e) {
        // log.servletContextDestroyed(e.getServletContext());
        servletCtx = null;
    }
	
    public ServletContext getServletContext() {
        return servletCtx;
    }
    
    public static class InternalServletContextEvent {
        private ServletContext ctx;

        public InternalServletContextEvent(ServletContext ctx) {
            this.ctx = ctx;
        }

        public ServletContext getServletContext() {
            return ctx;
        }
    }
    
    public void destroySipContexts(){
//    	beanManager.fireEvent(event, qualifiers);
    }
}

