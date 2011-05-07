package org.mobicents.servlet.sip.weld.extension.context.sip.beanstore;


import static java.util.Collections.emptyList;
import static org.jboss.weld.logging.Category.CONTEXT;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.beanstore.AttributeBeanStore;
import org.jboss.weld.context.beanstore.NamingScheme;
import org.jboss.weld.context.beanstore.http.AbstractSessionBeanStore;
import org.jboss.weld.util.collections.EnumerationList;
import org.jboss.weld.util.reflection.Reflections;
import org.slf4j.cal10n.LocLogger;

public abstract class AbstractSipSessionBeanStore extends AttributeBeanStore
{

	   private static final LocLogger log = loggerFactory().getLogger(CONTEXT);

	   protected abstract SipSession getSession(boolean create);

	   public AbstractSipSessionBeanStore(NamingScheme namingScheme)
	   {
	      super(namingScheme);
	   }

	   protected Collection<String> getAttributeNames()
	   {
	      SipSession session = getSession(false);
	      if (session == null)
	      {
	         return emptyList();
	      }
	      else
	      {
	         return new EnumerationList<String>(Reflections.<Enumeration<String>>cast(session.getAttributeNames()));
	      }
	   }

	   @Override
	   protected void removeAttribute(String key)
	   {
	      SipSession session = getSession(false);
	      if (session != null)
	      {
	         session.removeAttribute(key);
	         log.trace("Removed " + key + " from session " + this.getSession(false).getId());
	      }
	      else
	      {
	         log.trace("Unable to remove " + key + " from non-existent session");
	      }
	   }

	   @Override
	   protected void setAttribute(String key, Object instance)
	   {
	      SipSession session = getSession(true);
	      if (session != null)
	      {
	         session.setAttribute(key, instance);
	         log.trace("Added " + key + " to session " + this.getSession(false).getId());
	      }
	      else
	      {
	         log.trace("Unable to add " + key + " to session as no session could be obtained");
	      }
	   }

	   @Override
	   protected Object getAttribute(String prefixedId)
	   {
	      return getSession(false).getAttribute(prefixedId);
	   }

	}