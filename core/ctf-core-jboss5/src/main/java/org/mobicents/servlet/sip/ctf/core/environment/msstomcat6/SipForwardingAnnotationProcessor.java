package org.mobicents.servlet.sip.ctf.core.environment.msstomcat6;

import java.lang.reflect.InvocationTargetException;

import javax.naming.NamingException;

import org.apache.AnnotationProcessor;
import org.jboss.weld.environment.tomcat.ForwardingAnnotationProcessor;

public abstract class SipForwardingAnnotationProcessor extends ForwardingAnnotationProcessor implements AnnotationProcessor
{
	   
	   protected abstract AnnotationProcessor delegate();
	   
	   public void postConstruct(Object instance) throws IllegalAccessException, InvocationTargetException
	   {
	      delegate().postConstruct(instance);
	   }
	   
	   public void preDestroy(Object instance) throws IllegalAccessException, InvocationTargetException
	   {
	      delegate().preDestroy(instance);
	   }
	   
	   public void processAnnotations(Object instance) throws IllegalAccessException, InvocationTargetException, NamingException
	   {
	      delegate().processAnnotations(instance);
	   }
	   
	}