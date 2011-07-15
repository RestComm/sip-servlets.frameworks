package org.mobicents.servlet.sip.ctf.core.extension.event.session.literal;

import javax.enterprise.util.AnnotationLiteral;

import org.mobicents.servlet.sip.ctf.core.extension.event.session.SipSessionEv;

public class SipSessionLiteral extends AnnotationLiteral<SipSessionEv> implements SipSessionEv {
    private static final long serialVersionUID = 1340645410138297667L;
    public static final SipSessionEv INSTANCE = new SipSessionLiteral();
}