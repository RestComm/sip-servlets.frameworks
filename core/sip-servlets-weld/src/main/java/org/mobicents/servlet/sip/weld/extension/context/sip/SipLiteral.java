package org.mobicents.servlet.sip.weld.extension.context.sip;

import javax.enterprise.util.AnnotationLiteral;

public class SipLiteral extends AnnotationLiteral<Sip> implements Sip 
{

   public static final Sip INSTANCE = new SipLiteral();
   
   private SipLiteral() {}

}
