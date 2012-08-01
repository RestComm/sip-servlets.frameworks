package org.mobicents.servlet.sip.ctf.core.extension.context.sip;

import javax.enterprise.util.AnnotationLiteral;

/*
* @author gvagenas 
* gvagenas@gmail.com / devrealm.org
*/

public class SipLiteral extends AnnotationLiteral<Sip> implements Sip 
{

   public static final Sip INSTANCE = new SipLiteral();
   
   private SipLiteral() {}

}
