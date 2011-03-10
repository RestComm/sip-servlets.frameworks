package org.mobicents.servlet.sip.weld.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.servlet.sip.SipFactory;

import org.mobicents.servlet.sip.message.SipFactoryFacade;

public class SipUtilitiesExtension implements Extension{

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		
		AnnotatedType<SipUtilitiesExtensionManagedBean> at = bm.createAnnotatedType(SipUtilitiesExtensionManagedBean.class);
		
		final InjectionTarget<SipUtilitiesExtensionManagedBean> it = bm.createInjectionTarget(at);
		
		abd.addBean(new Bean<SipUtilitiesExtensionManagedBean>() {

			@Override
			public Class<?> getBeanClass() {
				return SipUtilitiesExtensionManagedBean.class;
			}
			
			@Override
			public Set<InjectionPoint> getInjectionPoints() {
				return it.getInjectionPoints();
			}
			
			@Override
			public String getName() {
				return "javax.servlet.sip.SipFactory";
			}
			
			@SuppressWarnings("serial")
			@Override
			public Set<Annotation> getQualifiers() {
				Set<Annotation> qualifiers = new HashSet<Annotation>();

				qualifiers.add(new AnnotationLiteral<Default>() {});
				qualifiers.add(new AnnotationLiteral<Any>() {});

				return qualifiers;
			}
			
			@Override
			public Class<? extends Annotation> getScope() {
				return ApplicationScoped.class;
			}
			
			@Override
			public Set<Class<? extends Annotation>> getStereotypes() {

				return Collections.emptySet();
			}
			
			@Override
			public Set<Type> getTypes() {

				Set<Type> types = new HashSet<Type>();

				types.add(SipUtilitiesExtensionManagedBean.class);
				types.add(Object.class);

				return types;

			}
			
			@Override
			public SipUtilitiesExtensionManagedBean create(CreationalContext<SipUtilitiesExtensionManagedBean> creationalContext) {		
				return it.produce(creationalContext);
			}

			@Override
			public void destroy(SipUtilitiesExtensionManagedBean instance,
					CreationalContext<SipUtilitiesExtensionManagedBean> creationalContext) {
				it.dispose(instance);
				creationalContext.release();
			}

			@Override
			public boolean isAlternative() {
				return false;
			}

			@Override
			public boolean isNullable() {
				return false;
			}
			
		});
		
	}
	
}
