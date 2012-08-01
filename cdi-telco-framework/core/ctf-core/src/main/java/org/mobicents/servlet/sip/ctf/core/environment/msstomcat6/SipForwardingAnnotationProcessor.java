package org.mobicents.servlet.sip.ctf.core.environment.msstomcat6;

import java.lang.reflect.InvocationTargetException;

import javax.naming.NamingException;

import org.apache.AnnotationProcessor;
import org.jboss.weld.environment.tomcat.ForwardingAnnotationProcessor;
import org.mobicents.servlet.sip.annotations.SipAnnotationProcessor;

/*
 * Issue 2779 - http://code.google.com/p/mobicents/issues/detail?id=2779
 * SipForwardingAnnotationProcessor needs to implement SipAnnotationProcessor in order to remain after
 * super.start() at SipStandardContext.start() method
 */

public abstract class SipForwardingAnnotationProcessor extends ForwardingAnnotationProcessor implements SipAnnotationProcessor
{
	   
	   protected abstract AnnotationProcessor delegate();
	   
	   @Override
	   public void postConstruct(Object instance) throws IllegalAccessException, InvocationTargetException
	   {
	      delegate().postConstruct(instance);
	   }
	   
	   @Override
	   public void preDestroy(Object instance) throws IllegalAccessException, InvocationTargetException
	   {
	      delegate().preDestroy(instance);
	   }
	   
	   @Override
	   public void processAnnotations(Object instance) throws IllegalAccessException, InvocationTargetException, NamingException
	   {
	      delegate().processAnnotations(instance);
	   }
	   
	}