/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mobicents.servlet.sip.cdi.environment.servlet;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

import javax.el.ELContextListener;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.environment.jetty.JettyWeldInjector;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;
import org.jboss.weld.environment.servlet.deployment.URLScanner;
import org.jboss.weld.environment.servlet.deployment.VFSURLScanner;
import org.jboss.weld.environment.servlet.services.ServletResourceInjectionServices;
import org.jboss.weld.environment.servlet.util.Reflections;
import org.jboss.weld.environment.tomcat.WeldForwardingAnnotationProcessor;
import org.jboss.weld.environment.tomcat7.WeldForwardingInstanceManager;
import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.manager.api.WeldManager;
import org.jboss.weld.servlet.api.ServletListener;
import org.jboss.weld.servlet.api.helpers.ForwardingServletListener;
import org.mobicents.servlet.sip.cdi.environment.msstomcat7.SipWeldForwardingInstanceManager;
import org.mobicents.servlet.sip.startup.ConvergedApplicationContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pete Muir
 * @author Ales Justin
 */
public class SipServletsWeldListener extends ForwardingServletListener 
{
	private static final Logger log = LoggerFactory.getLogger(SipServletsWeldListener.class);

	private static final String BOOTSTRAP_IMPL_CLASS_NAME = "org.jboss.weld.bootstrap.WeldBootstrap";
	private static final String WELD_LISTENER_CLASS_NAME = "org.jboss.weld.servlet.WeldListener"; 
	private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";
	private static final String JETTY_REQUIRED_CLASS_NAME = "org.mortbay.jetty.servlet.ServletHandler";
	public  static final String INJECTOR_ATTRIBUTE_NAME = "org.jboss.weld.environment.jetty.JettyWeldInjector";
	public static final String BEAN_MANAGER_ATTRIBUTE_NAME = SipServletsWeldListener.class.getPackage().getName() + "." + BeanManager.class.getName();

	private final transient Bootstrap bootstrap;
	private final transient ServletListener weldListener;

	public SipServletsWeldListener()
	{
		try
		{
			bootstrap = Reflections.newInstance(BOOTSTRAP_IMPL_CLASS_NAME);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalStateException("Error loading Weld bootstrap, check that Weld is on the classpath", e);
		}
		try
		{
			weldListener = Reflections.newInstance(WELD_LISTENER_CLASS_NAME);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalStateException("Error loading Weld listener, check that Weld is on the classpath", e);
		}
	}

	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		bootstrap.shutdown();
		try
		{
			Reflections.classForName("org.apache.AnnotationProcessor");
			WeldForwardingAnnotationProcessor.restoreAnnotationProcessor(sce);
		}
		catch (IllegalArgumentException ignore) {}
		try
		{
			Reflections.classForName(JETTY_REQUIRED_CLASS_NAME);
			sce.getServletContext().removeAttribute(INJECTOR_ATTRIBUTE_NAME);
		}
		catch (IllegalArgumentException ignore) {}
		super.contextDestroyed(sce);
	}

	/**
	 * Create server deployment.
	 *
	 * Can be overridden with custom servlet deployment.
	 * e.g. exact resources listing in ristricted wnv like GAE
	 *
	 * @param context the servlet context
	 * @param bootstrap the bootstrap
	 * @return new servlet deployment
	 */
	protected ServletDeployment createServletDeployment(ServletContext context, Bootstrap bootstrap)
	{
		return new ServletDeployment(context, bootstrap);
	}

	/**
	 * Get appropriate scanner.
	 * Return null to leave it to defaults.
	 *
	 * @param classLoader the classloader
	 * @param context the servlet context
	 * @return custom url scanner or null if we should use default
	 */
	protected URLScanner createUrlScanner(ClassLoader classLoader, ServletContext context)
	{
		try
		{
			classLoader.loadClass("org.jboss.virtual.VFS"); // check if we can use JBoss VFS
			return new VFSURLScanner(classLoader);
		}
		catch (Throwable t)
		{
			return null;
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		// Make Javassist always use the TCCL to load classes
		ProxyFactory.classLoaderProvider = new ClassLoaderProvider()
		{

			public ClassLoader get(ProxyFactory pf)
			{
				return Thread.currentThread().getContextClassLoader();
			}

		};

		ClassLoader classLoader = Reflections.getClassLoader(); 
		ServletContext context = sce.getServletContext();

		URLScanner scanner = createUrlScanner(classLoader, context);
		if (scanner != null)
		{
			context.setAttribute(URLScanner.class.getName(), scanner);
		}

		ServletDeployment deployment = createServletDeployment(context, bootstrap);
		try
		{
			deployment.getWebAppBeanDeploymentArchive().getServices().add(
					ResourceInjectionServices.class, new ServletResourceInjectionServices() {});
		}
		catch (NoClassDefFoundError e)
		{
			// Support GAE
			log.warn("@Resource injection not available in simple beans");
		}

		bootstrap.startContainer(Environments.SERVLET, deployment).startInitialization();
		WeldManager manager = bootstrap.getManager(deployment.getWebAppBeanDeploymentArchive());

		boolean tomcat = true;
		boolean mss1tomcat = false;
		boolean tomcat7 = false;
		boolean mss2tomcat = false;
		try
		{
			Reflections.classForName("org.apache.AnnotationProcessor");
			if (sce.getServletContext() instanceof ConvergedApplicationContextFacade){
				mss1tomcat = true;
				tomcat = false;
			} else {
				tomcat = true; 
			}
		}
		catch (IllegalArgumentException e)
		{
			tomcat = false;
			mss1tomcat = false;
		}

		if(mss1tomcat){
			WeldForwardingAnnotationProcessor.replaceAnnotationProcessor(sce, manager);
			log.info("MSS 1.x on Tomcat 6 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
		}
		
		if (tomcat)
		{
			try
			{
				WeldForwardingAnnotationProcessor.replaceAnnotationProcessor(sce, manager);
				log.info("Tomcat 6 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
			}
			catch (Exception e)
			{
				log.error("Unable to replace Tomcat AnnotationProcessor. CDI injection will not be available in Servlets, Filters, or Listeners", e);
			}
		}
		try
		{
			Reflections.classForName("org.apache.tomcat.InstanceManager");
			if (sce.getServletContext() instanceof ConvergedApplicationContextFacade){
				mss2tomcat = true;
			} else {
				tomcat7 = true; 
			}
		}
		catch (IllegalArgumentException e)
		{
			tomcat7 = false;
			mss2tomcat = false;
		}

		boolean jetty = true;
		try
		{
			Reflections.classForName(JETTY_REQUIRED_CLASS_NAME);
		}
		catch (IllegalArgumentException e)
		{
			jetty = false;
		}

		if (jetty)
		{
			// Try pushing a Jetty Injector into the servlet context
			try
			{
				Class<?> clazz = Reflections.classForName(JettyWeldInjector.class.getName());
				Object injector = clazz.getConstructor(WeldManager.class).newInstance(manager);
				context.setAttribute(INJECTOR_ATTRIBUTE_NAME, injector);
				log.info("Jetty detected, JSR-299 injection will be available in Servlets and Filters. Injection into Listeners is not supported.");
			}
			catch (Exception e)
			{
				log.error("Unable to create JettyWeldInjector. CDI injection will not be available in Servlets, Filters or Listeners", e);
			}
		}
		if (tomcat7)
		{
			try
			{
				WeldForwardingInstanceManager.replacInstanceManager(sce, manager);
				log.info("Tomcat 7 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
			}
			catch (Exception e)
			{
				log.error("Unable to replace Tomcat 7 AnnotationProcessor. CDI injection will not be available in Servlets, Filters, or Listeners", e);
			}
		}

		if (mss2tomcat)
		{
			try
			{
				SipWeldForwardingInstanceManager.replacInstanceManager(sce, manager);
				log.info("MSS2 on top of Tomcat 7 detected, CDI injection will be available in Servlets and Filters. Injection into Listeners is not supported");
			}
			catch (Exception e)
			{
				log.error("Unable to replace MSS 2 Tomcat 7 AnnotationProcessor. CDI injection will not be available in Servlets, Filters, or Listeners", e);
			}
		}

		if (!tomcat && !jetty&&!tomcat7&&!mss2tomcat) {
			log.info("No supported servlet container detected, CDI injection will NOT be available in Servlets, Filtersor or Listeners");
		}

		// Push the manager into the servlet context so we can access in JSF
		context.setAttribute(BEAN_MANAGER_ATTRIBUTE_NAME, manager);

		//Causes java.lang.LinkageError: loader constraint violation: when resolving interface method 
		//"javax.servlet.jsp.JspApplicationContext.addELResolver(Ljavax/el/ELResolver;)V" 
		//Dont proceed if we are in MSS
		//TODO: Resolve this issue
		if(!mss2tomcat){
			if (JspFactory.getDefaultFactory() != null)
			{
				JspApplicationContext jspApplicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(context);

				// Register the ELResolver with JSP
				jspApplicationContext.addELResolver(manager.getELResolver());

				// Register ELContextListener with JSP
				jspApplicationContext.addELContextListener(Reflections.<ELContextListener>
				newInstance("org.jboss.weld.el.WeldELContextListener"));

				// Push the wrapped expression factory into the servlet context so that Tomcat or Jetty can hook it in using a container code
				context.setAttribute(EXPRESSION_FACTORY_NAME,
						manager.wrapExpressionFactory(jspApplicationContext.getExpressionFactory()));
			}
		}
		
		bootstrap.deployBeans().validateBeans().endInitialization();
		super.contextInitialized(sce);
	}

	@Override
	protected ServletListener delegate()
	{
		return weldListener;
	}
}
