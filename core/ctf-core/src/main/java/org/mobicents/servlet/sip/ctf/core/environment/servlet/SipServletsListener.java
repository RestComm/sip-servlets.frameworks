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
package org.mobicents.servlet.sip.ctf.core.environment.servlet;

import java.util.Arrays;
import java.util.ServiceLoader;

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
import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;
import org.jboss.weld.environment.servlet.deployment.URLScanner;
import org.jboss.weld.environment.servlet.deployment.VFSURLScanner;
import org.jboss.weld.environment.servlet.util.Reflections;
import org.jboss.weld.environment.tomcat.Tomcat6Container;
import org.jboss.weld.environment.tomcat7.Tomcat7Container;
import org.jboss.weld.injection.spi.ResourceInjectionServices;
import org.jboss.weld.manager.api.WeldManager;
import org.jboss.weld.servlet.api.ServletListener;
import org.jboss.weld.servlet.api.helpers.ForwardingServletListener;
import org.mobicents.servlet.sip.ctf.core.environment.mssjboss5.MSSJBoss5Container;
import org.mobicents.servlet.sip.ctf.core.environment.msstomcat6.MSSTomcat6Container;
import org.mobicents.servlet.sip.ctf.core.environment.msstomcat7.MSSTomcat7Container;
import org.mobicents.servlet.sip.ctf.core.environment.services.SipServletResourceInjectionServices;
import org.mobicents.servlet.sip.ctf.core.extension.SipServletObjectsHolder.InternalServletContextEvent;
import org.mobicents.servlet.sip.ctf.core.extension.event.context.literal.DestroyedLiteral;
import org.mobicents.servlet.sip.ctf.core.extension.event.context.literal.InitializedLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pete Muir
 * @author Ales Justin
 * @author George Vagenas
 */

/*
 * Main CTF listener, will bootstrap CDI for Converged applications
 *  
 * gvagenas@gmail.com / devrealm.org
 */

public class SipServletsListener extends ForwardingServletListener 
{
	private static final Logger log = LoggerFactory.getLogger(SipServletsListener.class);

	private static final String BOOTSTRAP_IMPL_CLASS_NAME = "org.jboss.weld.bootstrap.WeldBootstrap";
	private static final String WELD_LISTENER_CLASS_NAME = "org.jboss.weld.servlet.WeldListener"; 
	private static final String EXPRESSION_FACTORY_NAME = "org.jboss.weld.el.ExpressionFactory";
	private static final String JETTY_REQUIRED_CLASS_NAME = "org.mortbay.jetty.servlet.ServletHandler";
	public  static final String INJECTOR_ATTRIBUTE_NAME = "org.jboss.weld.environment.jetty.JettyWeldInjector";
	// CTF Issue http://code.google.com/p/mobicents/issues/detail?id=2601
	// CTF beans cannot be resolved in JSF because the BeanManager name was wrong
	public static final String BEAN_MANAGER_ATTRIBUTE_NAME = Listener.class.getPackage().getName() + "." + BeanManager.class.getName();

	private final transient Bootstrap bootstrap;
	private final transient ServletListener weldListener;
	private transient WeldManager manager; 
	private Container container;

	public SipServletsListener()
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
		manager.fireEvent(new InternalServletContextEvent(sce.getServletContext()), DestroyedLiteral.INSTANCE);

		bootstrap.shutdown();

		if (container != null)
			container.destroy(new ContainerContext(sce, null));

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
			//Issue 2822 -  http://code.google.com/p/mobicents/issues/detail?id=2822
			//Provide our custom ResourceInjectionService
			deployment.getWebAppBeanDeploymentArchive().getServices().add(
					ResourceInjectionServices.class, new SipServletResourceInjectionServices(context) {});
		}
		catch (NoClassDefFoundError e)
		{
			// Support GAE
			log.warn("@Resource injection not available in simple beans");
		}

		bootstrap.startContainer(Environments.SERVLET, deployment).startInitialization();
		manager = bootstrap.getManager(deployment.getWebAppBeanDeploymentArchive());

		ContainerContext cc = new ContainerContext(sce, manager);
		StringBuilder dump = new StringBuilder();
		Container container = findContainer(cc, dump);
		if (container == null)
		{
			log.info("No supported servlet container detected, CDI injection will NOT be available in Servlets, Filtersor or Listeners");
			if (log.isDebugEnabled())
				log.debug("Exception dump from Container lookup: " + dump);
		}
		else
		{
			container.initialize(cc);
			this.container = container;
		}

		// Push the manager into the servlet context so we can access in JSF
		context.setAttribute(BEAN_MANAGER_ATTRIBUTE_NAME, manager);

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

		bootstrap.deployBeans().validateBeans().endInitialization();
		super.contextInitialized(sce);

		//Initialize SipServlets tools
		//		ConvergedApplication convergedApplication = new ConvergedApplication(sce.getServletContext());
		manager.fireEvent(new InternalServletContextEvent(sce.getServletContext()), InitializedLiteral.INSTANCE);
	}

	@Override
	protected ServletListener delegate()
	{
		return weldListener;
	}

	/**
	 * Find container env.
	 *
	 * @param cc the container context
	 * @param dump the exception dump
	 * @return valid container or null
	 */
	protected Container findContainer(ContainerContext cc, StringBuilder dump)
	{
		ServiceLoader<Container> extContainers = ServiceLoader.load(Container.class, getClass().getClassLoader());
		Container container = checkContainers(cc, dump, extContainers);
		
		//First check for MSS on JBoss5 container
		if (container == null)
			container = checkContainers(cc, dump, Arrays.asList(
					MSSJBoss5Container.INSTANCE)
					);
		
		//If MSS-JBoss5 not found, continue for the rest of the containers that we support
		if (container == null)
			container = checkContainers(cc, dump, Arrays.asList(
					//Order is important of the containers, first check for MSS-Tomcat and then for Tomcat
					MSSTomcat7Container.INSTANCE,
					Tomcat7Container.INSTANCE,
					MSSTomcat6Container.INSTANCE,
					Tomcat6Container.INSTANCE)
					);

		return container;
	}

	protected Container checkContainers(ContainerContext cc, StringBuilder dump, Iterable<Container> containers)
	{
		for (Container c : containers)
		{
			try
			{
				if (c.touch(cc))
					return c;
			}
			catch (Throwable t)
			{
				dump.append(c).append("->").append(t.getMessage()).append("\n");
			}
		}
		return null;
	}

}
