package org.mobicents.servlet.sip.weld.extension.context.sip.beanstore.sipsession;

import static org.jboss.weld.logging.Category.CONTEXT;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;

import javax.servlet.sip.SipSession;

import org.jboss.weld.context.beanstore.NamingScheme;
import org.slf4j.cal10n.LocLogger;

public class EagerSipSessionBeanStore extends AbstractSipSessionBeanStore
{
	   private static final LocLogger log = loggerFactory().getLogger(CONTEXT);

	   private final SipSession session;

	   public EagerSipSessionBeanStore(NamingScheme namingScheme, SipSession session)
	   {
	      super(namingScheme);
	      this.session = session;
	      log.trace("Loading bean store " + this + " map from session " + getSession(false));
	   }

	   @Override
	   protected SipSession getSession(boolean create)
	   {
	      return session;
	   }

	}
