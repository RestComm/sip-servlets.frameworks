package org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipappsession;

import static org.jboss.weld.logging.Category.CONTEXT;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.beanstore.NamingScheme;
import org.slf4j.cal10n.LocLogger;

public class LazySipApplicationSessionBeanStore extends AbstractSipApplicationSessionBeanStore
{

	   private static final LocLogger log = loggerFactory().getLogger(CONTEXT);
	   
	   private final SipServletRequest request;

	   public LazySipApplicationSessionBeanStore(SipServletRequest request, NamingScheme namingScheme)
	   {
	      super(namingScheme);
	      this.request = request;
	      log.trace("Loading bean store " + this + " map from session " + getSession(false));
	   }

	   @Override
	   protected SipApplicationSession getSession(boolean create)
	   {
	      try
	      {
	         return request.getApplicationSession(create);
	      }
	      catch (IllegalStateException e)
	      {
	         // If container can't create an underlying session, invalidate the
	         // current one
	         detach();
	         return null;
	      }
	   }
}