package org.mobicents.servlet.sip.ctf.core.environment.msstomcat6;

import java.lang.reflect.InvocationTargetException;

import javax.naming.NamingException;

import org.apache.AnnotationProcessor;
import org.jboss.weld.environment.servlet.inject.AbstractInjector;
import org.jboss.weld.manager.api.WeldManager;

public class SipWeldAnnotationProcessor extends AbstractInjector implements AnnotationProcessor
{
	   public SipWeldAnnotationProcessor(WeldManager manager)
	   {
	      super(manager);
	   }

	   public void processAnnotations(Object instance) throws IllegalAccessException, InvocationTargetException, NamingException
	   {
	      inject(instance);
	   }

	   public void postConstruct(Object arg0) throws IllegalAccessException, InvocationTargetException
	   {
	   }
	   
	   public void preDestroy(Object arg0) throws IllegalAccessException, InvocationTargetException
	   {
	   }
}