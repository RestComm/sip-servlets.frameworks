package org.mobicents.servlet.sip.cdi.environment.msstomcat7;

import static org.jboss.weld.environment.servlet.util.Reflections.findDeclaredField;
import static org.jboss.weld.environment.servlet.util.Reflections.findDeclaredMethod;
import static org.jboss.weld.environment.servlet.util.Reflections.getFieldValue;
import static org.jboss.weld.environment.servlet.util.Reflections.invokeMethod;
import static org.jboss.weld.environment.servlet.util.Reflections.setFieldValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.InstanceManager;
import org.jboss.weld.environment.tomcat7.WeldForwardingInstanceManager;
import org.jboss.weld.manager.api.WeldManager;
import org.mobicents.servlet.sip.annotations.SipInstanceManager;
import org.mobicents.servlet.sip.startup.ConvergedApplicationContextFacade;


public class SipWeldForwardingInstanceManager extends SipForwardingInstanceManager { 

	   private final SipInstanceManager firstProcessor;
	   private final SipInstanceManager secondProcessor;

	   
	   public SipWeldForwardingInstanceManager(SipInstanceManager originalAnnotationProcessor, SipInstanceManager weldProcessor)
	   {
//		  super(originalAnnotationProcessor,weldProcessor);
	      this.firstProcessor = originalAnnotationProcessor;
	      this.secondProcessor = weldProcessor;
	   }

	   @Override
	   protected SipInstanceManager delegate()
	   {
	      return firstProcessor;
	   }

	   @Override
	   public void destroyInstance(Object o) throws IllegalAccessException, InvocationTargetException
	   {
	      super.destroyInstance(o);
	      secondProcessor.destroyInstance(o);
	   }

	   @Override
	   public void newInstance(Object o) throws IllegalAccessException, InvocationTargetException, NamingException
	   {
	      super.newInstance(o);
	      secondProcessor.newInstance(o);
	   }

	   @Override
	   public Object newInstance(String fqcn, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException
	   {
	      Object a = super.newInstance(fqcn, classLoader);
	      secondProcessor.newInstance(a);
	      return a;
	   }

	   @Override
	   public Object newInstance(String fqcn) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException
	   {
	      Object a = super.newInstance(fqcn);
	      secondProcessor.newInstance(a);
	      return a;
	   }

	   public static void replacInstanceManager(ServletContextEvent sce, WeldManager manager)
	   {
	      StandardContext stdContext = getStandardContext(sce);
	      setInstanceManager(stdContext, createInstance(manager, stdContext));
	      
	      /*
	       * Inject a SipServlet to initialize SipFactory
	       */
	    		  
	      
//	      if (stdContext instanceof SipStandardContext){
//			  String injectedServletClass = "org.mobicents.servlet.sip.weld.InjectedSipServices.InjectedSipServlet";
////	    	  String injectedServletClass = "org.mobicents.servlet.sip.weld.weldentrypoint.WeldEntryPointServlet";
//			  Object injectedServlet = 
//				  org.jboss.weld.environment.servlet.util.Reflections.newInstance(injectedServletClass);
//			  
//			  SipServletImpl parsedServletData = (SipServletImpl) stdContext.createWrapper();
//			  
//				parsedServletData.setName("injectedServlet");
//				parsedServletData.setServletName("injectedServlet");
//				parsedServletData.setDisplayName("injectedServlet");
//				parsedServletData.setDescription("injectedServlet");
//				parsedServletData.setServletClass(injectedServlet.getClass().getCanonicalName());
//				parsedServletData.setLoadOnStartup(1);
//				parsedServletData.setParent(stdContext);
//				((SipStandardContext) stdContext).addChild(parsedServletData);
//			  
//				((SipStandardContext) stdContext).setMainServlet(parsedServletData.getName());
//	      }
	   }

	private static SipWeldForwardingInstanceManager createInstance(WeldManager manager, StandardContext stdContext)
	   {
	      try
	      {

	         SipInstanceManager weldProcessor = new SipWeldInstanceManager(manager);
	         return new SipWeldForwardingInstanceManager(getInstanceManager(stdContext), weldProcessor);
	      }
	      catch (Exception e)
	      {
	         throw new RuntimeException("Cannot create WeldForwardingAnnotationProcessor", e);
	      }
	   }

	   private static StandardContext getStandardContext(ServletContextEvent sce)
	   {
	      try
	      {
	         // Hack into Tomcat to replace the InstanceManager using
	         // reflection to access private fields
	    	  ServletContext sc = sce.getServletContext();
	    	  ApplicationContext appContext = null;
//	    	  appContext =  
//    			  (ApplicationContext) getContextFieldValue((ConvergedApplicationContextFacade) sce.getServletContext(), ConvergedApplicationContextFacade.class);
	    	  if (sc instanceof ApplicationContextFacade) {
	    		  appContext = 
	    			  (ApplicationContext) getContextFieldValue((ApplicationContextFacade) sce.getServletContext(), ApplicationContextFacade.class);
	    	  } else if (sc instanceof ConvergedApplicationContextFacade){
	    		  appContext =  
	    			  (ApplicationContext) getContextFieldValue((ConvergedApplicationContextFacade) sce.getServletContext(), ConvergedApplicationContextFacade.class);
	    	  } 
	         
	         return (StandardContext) getContextFieldValue(appContext, ApplicationContext.class);
	      }
	      catch (Exception e)
	      {
	         throw new RuntimeException("Cannot get StandardContext from ServletContext", e);
	      }
	   }

	   private static <E> Object getContextFieldValue(E obj, Class<E> clazz) throws NoSuchFieldException, IllegalAccessException
	   {
	      Field f = clazz.getDeclaredField("context");
	      f.setAccessible(true);
	      return f.get(obj);
	   }

	   public static void restoreInstanceManager(ServletContextEvent sce)
	   {
	      StandardContext stdContext = getStandardContext(sce);
	      InstanceManager im = getInstanceManager(stdContext);
	      if (im instanceof WeldForwardingInstanceManager)
	      {
	         setInstanceManager(stdContext, ((SipWeldForwardingInstanceManager) im).firstProcessor);
	      }
	   }

	   private static SipInstanceManager getInstanceManager(StandardContext stdContext)
	   {

	      Method method = findDeclaredMethod(stdContext.getClass(), "getInstanceManager");
	      if (method != null)
	      {
	         return invokeMethod(method, SipInstanceManager.class, stdContext);
	      }
	      Field field = findDeclaredField(stdContext.getClass(), "instanceManager");
	      if (field != null)
	      {
	         return getFieldValue(field, stdContext, SipInstanceManager.class);
	      }
	      throw new RuntimeException("neither field nor setter found for instanceManager");
	   }

	   private static void setInstanceManager(StandardContext stdContext, InstanceManager instanceManager)
	   {

	      Method method = findDeclaredMethod(stdContext.getClass(), "setInstanceManager", InstanceManager.class);
	      if (method != null)
	      {
	         invokeMethod(method, void.class, stdContext, instanceManager);
	         return;
	      }
	      Field field = findDeclaredField(stdContext.getClass(), "instanceManager");
	      if (field != null)
	      {
	         setFieldValue(field, stdContext, instanceManager);
	         return;
	      }
	      throw new RuntimeException("neither field nor setter found for instanceManager");
	   }

	@Override
	public void processAnnotations(Object instance,
			Map<String, String> injections) throws IllegalAccessException,
			InvocationTargetException, NamingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getInjectionMap(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
