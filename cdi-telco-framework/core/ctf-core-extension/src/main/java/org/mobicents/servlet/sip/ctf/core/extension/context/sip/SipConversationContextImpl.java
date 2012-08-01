package org.mobicents.servlet.sip.ctf.core.extension.context.sip;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;

import org.jboss.weld.context.AbstractConversationContext;
import org.jboss.weld.context.beanstore.BoundBeanStore;
import org.jboss.weld.context.beanstore.NamingScheme;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.beanstore.EagerSipSessionBeanStore;
import org.mobicents.servlet.sip.ctf.core.extension.context.sip.beanstore.LazySipSessionBeanStore;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

public class SipConversationContextImpl extends AbstractConversationContext<SipServletRequest, SipSession> implements SipConversationContext
{

	   @Override
	   protected void setSessionAttribute(SipServletRequest request, String name, Object value, boolean create)
	   {
	      if (create || request.getSession(false) != null)
	      {
	         request.getSession(true).setAttribute(name, value);
	      }
	   }

	   @Override
	   protected Object getSessionAttribute(SipServletRequest request, String name, boolean create)
	   {
	      if (create || request.getSession(false) != null)
	      {
	         return request.getSession(true).getAttribute(name);
	      }
	      else
	      {
	         return null;
	      }
	   }

	   @Override
	   protected void removeRequestAttribute(SipServletRequest request, String name)
	   {
	      request.removeAttribute(name);
	   }

	   @Override
	   protected void setRequestAttribute(SipServletRequest request, String name, Object value)
	   {
	      request.setAttribute(name, value);
	   }

	   @Override
	   protected Object getRequestAttribute(SipServletRequest request, String name)
	   {
	      return request.getAttribute(name);
	   }

	   @Override
	   protected BoundBeanStore createRequestBeanStore(NamingScheme namingScheme, SipServletRequest request)
	   {
	      return new LazySipSessionBeanStore(request, namingScheme);
	   }
	  
	   @Override
	   protected BoundBeanStore createSessionBeanStore(NamingScheme namingScheme, SipSession session)
	   {
	      return new EagerSipSessionBeanStore(namingScheme, session);
	   }

	   @Override
	   protected Object getSessionAttributeFromSession(SipSession session, String name)
	   {
	      return session.getAttribute(name);
	   }

	   @Override
	   protected SipSession getSessionFromRequest(SipServletRequest request, boolean create)
	   {
	      return request.getSession(create);
	   }
	
}
