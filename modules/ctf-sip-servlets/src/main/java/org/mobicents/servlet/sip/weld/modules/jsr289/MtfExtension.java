/**
 * 
 */
package org.mobicents.servlet.sip.ctf.modules.jsr289;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.mobicents.framework.servlet.sip.services.SipService;

/**
 * @author gvagenas 
 * gvagenas@gmail.com / devrealm.org
 * 
 */
public class MtfExtension implements Extension {

//	Set<Class<?>> getMtfClasses(){
//		Set<Class<?>> classes = new HashSet<Class<?>>();
//		classes.add(RegistrationService.class);
//		classes.add(Container.class);
//
//		return classes;
//	}
//
//	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
//
//		Set<Bean<?>> sipServiceBeans = bm.getBeans(SipService.class, new AnnotationLiteral<Any>() {});
//		Set<Bean<?>> containerBeans = bm.getBeans(Container.class, new AnnotationLiteral<Any>() {});
//
//	}

//	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
//
//		final AnnotatedType<T> type = pat.getAnnotatedType();
//
//		if ( SipService.class.isAssignableFrom( type.getJavaClass() ) ) {
//			//if the class implements Service, make it an @Alternative
//
//			AnnotatedType<T> wrapped = new AnnotatedType<T>() {
//				public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
//					return annotationType.equals(ApplicationScoped.class) ?
//							true : type.isAnnotationPresent(annotationType);
//				}
//
//				public <T extends Annotation> T getAnnotation(Class<T> arg0) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Set<Annotation> getAnnotations() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Type getBaseType() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Set<Type> getTypeClosure() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Set<AnnotatedConstructor<T>> getConstructors() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Set<AnnotatedField<? super T>> getFields() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Class<T> getJavaClass() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				public Set<AnnotatedMethod<? super T>> getMethods() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			};
//			pat.setAnnotatedType(wrapped);
//		}
//	}
}