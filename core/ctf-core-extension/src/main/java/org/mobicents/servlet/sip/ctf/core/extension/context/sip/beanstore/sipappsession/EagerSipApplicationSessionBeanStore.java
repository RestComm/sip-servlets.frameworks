package org.mobicents.servlet.sip.ctf.core.extension.context.sip.beanstore.sipappsession;

import static org.jboss.weld.logging.Category.CONTEXT;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.beanstore.NamingScheme;
import org.slf4j.cal10n.LocLogger;

public class EagerSipApplicationSessionBeanStore extends AbstractSipApplicationSessionBeanStore
{
	   private static final LocLogger log = loggerFactory().getLogger(CONTEXT);

	   private final SipApplicationSession session;

	   public EagerSipApplicationSessionBeanStore(NamingScheme namingScheme, SipApplicationSession session)
	   {
	      super(namingScheme);
	      this.session = session;
	      log.trace("Loading bean store " + this + " map from session " + getSession(false));
	   }

	   @Override
	   protected SipApplicationSession getSession(boolean create)
	   {
	      return session;
	   }

	}
